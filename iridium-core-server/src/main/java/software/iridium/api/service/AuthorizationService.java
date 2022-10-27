/*
 *  Copyright (C) Josh Fischer - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Josh Fischer <josh@joshfischer.io>, 2022.
 */

package software.iridium.api.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import software.iridium.api.base.error.BadRequestException;
import software.iridium.api.base.error.NotAuthorizedException;
import software.iridium.api.base.error.ResourceNotFoundException;
import software.iridium.api.generator.ProviderUrlGenerator;
import software.iridium.api.generator.RedirectUrlGenerator;
import software.iridium.api.generator.SuccessAuthorizationParameterGenerator;
import software.iridium.api.instantiator.*;
import software.iridium.api.repository.*;
import software.iridium.api.util.AttributeValidator;
import software.iridium.api.validator.AccessTokenRequestParameterValidator;
import software.iridium.api.validator.ApplicationEntityAccessTokenRequestValidator;
import software.iridium.api.validator.AuthorizationGrantTypeParamValidator;
import software.iridium.api.validator.AuthorizationRequestParameterValidator;
import software.iridium.api.authentication.client.ProviderAccessTokenRequestor;
import software.iridium.api.authentication.client.ProviderProfileRequestor;
import software.iridium.api.authentication.domain.AccessTokenResponse;
import software.iridium.api.authentication.domain.CodeChallengeMethod;
import software.iridium.api.authentication.domain.IdentityResponse;
import software.iridium.api.entity.ClientSecretEntity;
import software.iridium.api.entity.ExternalIdentityProviderEntity;
import software.iridium.api.mapper.AccessTokenResponseMapper;
import software.iridium.api.util.SHA256Hasher;
import software.iridium.api.util.AuthorizationCodeFlowConstants;
import software.iridium.api.util.ServletTokenExtractor;
import software.iridium.api.util.SubdomainExtractor;
import software.iridium.api.mapper.IdentityResponseMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import software.iridium.api.authentication.domain.ApplicationAuthorizationFormRequest;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Calendar;
import java.util.Map;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkArgument;

@Service
public class AuthorizationService {

    // todo may need to break this out into smaller classes
    @Resource
    private ProviderUrlGenerator providerUrlGenerator;
    @Resource
    private ProviderAccessTokenRequestor accessTokenRequestor;
    @Resource
    private ProviderProfileRequestor providerProfileRequestor;
    @Resource
    private IdentityEntityInstantiator identityInstantiator;
    @Resource
    private IdentityEntityRepository identityRepository;
    @Resource
    private IdentityEmailEntityRepository emailRepository;
    @Resource
    private IdentityResponseMapper identityResponseMapper;
    @Resource
    private AuthorizationGrantTypeParamValidator grantTypeValidator;
    @Resource
    private ApplicationEntityRepository applicationRepository;
    @Resource
    private RedirectUrlGenerator redirectUrlGenerator;
    @Resource
    private AttributeValidator attributeValidator;
    @Resource
    private AuthorizationCodeEntityInstantiator authCodeInstantiator;
    @Resource
    private AuthorizationCodeEntityRepository authCodeRepository;
    @Resource
    private AuthorizationRequestParameterValidator requestParameterValidator;
    @Resource
    private SuccessAuthorizationParameterGenerator successParamGenerator;
    @Resource
    private AccessTokenRequestParameterValidator accessTokenRequestParameterValidator;
    @Resource
    private ApplicationEntityAccessTokenRequestValidator applicationAccessTokenValidator;
    @Resource
    private SHA256Hasher sha256Hasher;
    @Resource
    private AccessTokenEntityInstantiator accessTokenInstantiator;
    @Resource
    private AccessTokenEntityRepository accessTokenRepository;
    @Resource
    private AccessTokenResponseMapper accessTokenResponseMapper;
    @Resource
    private RefreshTokenEntityInstantiator refreshTokenInstantiator;
    @Resource
    private SubdomainExtractor subdomainExtractor;
    @Resource
    private TenantEntityRepository tenantRepository;
    @Resource
    private AuthenticationEntityRepository authenticationRepository;
    @Resource
    private InProgressExternalIdentityProviderAuthorizationInstantiator inProgressAuthInstantiator;
    @Resource
    private InProgressExternalIdentityProviderAuthorizationEntityRepository inProgressAuthRepository;
    @Resource
    private ServletTokenExtractor tokenExtractor;
    @Resource
    private BCryptPasswordEncoder encoder;

    @Transactional(propagation = Propagation.REQUIRED)
    public IdentityResponse completeAuthorizationWithProvider(final String code, final String providerName, final String clientId, final String state) {
        checkArgument(attributeValidator.isNotBlank(code), "code must be not be blank");
        checkArgument(attributeValidator.isNotBlank(providerName), "providerName must be blank");
        checkArgument(attributeValidator.isNotBlankAndNoLongerThan(clientId, 32), "clientId must not be blank and no longer than 32 characters");
        checkArgument(attributeValidator.isNotBlank(state), "state must be not be blank");

        final var application = applicationRepository.findByClientId(clientId)
                .orElseThrow(() -> new ResourceNotFoundException("application not found for clientId: " + clientId));

        final var tenant = tenantRepository.findById(application.getTenantId())
                .orElseThrow(() -> new ResourceNotFoundException(String.format("tenant %s not found with clientId: %s", application.getTenantId(), clientId)));

        var providerFound = false;
        ExternalIdentityProviderEntity provider = null;
        for (ExternalIdentityProviderEntity providerEntity: tenant.getExternalIdentityProviders()) {
            if (providerEntity.getName().equalsIgnoreCase(providerName) && providerEntity.isActive()) {
                providerFound = true;
                provider = providerEntity;
            }
        }

        if (providerFound) {
            final var providerUrl = providerUrlGenerator.generate(provider, code);

            final var response = accessTokenRequestor.requestAccessToken(providerUrl);

            final var githubProfile = providerProfileRequestor.requestGithubProfile(provider.getProfileRequestBaseUrl(), response.getAccessToken());

            final var emailOptional = emailRepository.findByEmailAddressAndIdentity_ParentTenantId(githubProfile.getEmail(), tenant.getId());
            if (emailOptional.isEmpty()) {

                final var identity = identityInstantiator.instantiateFromGithub(githubProfile, provider);
                identity.getAuthorizedApplications().add(application);
                identity.setParentTenantId(tenant.getId());
                return identityResponseMapper.map(identityRepository.save(identity));
            }
            // todo: add logic for continuous property sync for external providers
            // todo: record login timestamps
            return identityResponseMapper.map(emailOptional.get().getIdentity());
        }
        throw new ResourceNotFoundException(String.format("active provider: %s not found for tenant: %s", providerName, tenant.getId()));
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public String authorize(final ApplicationAuthorizationFormRequest formRequest, final Map<String, String> params, final HttpServletRequest servletRequest) {
        grantTypeValidator.validate(params);

        final var subdomain = subdomainExtractor.extract(servletRequest.getRequestURL().toString());

        tenantRepository.findBySubdomain(subdomain)
                .orElseThrow(() -> new ResourceNotFoundException("tenant not found for subdomain " + subdomain));

        final var authentication = authenticationRepository
                .findFirstByAuthTokenAndExpirationAfter(formRequest.getUserToken(),  Calendar.getInstance().getTime())
                .orElseThrow(NotAuthorizedException::new);

        final var application = applicationRepository.findByClientId(params.get(AuthorizationCodeFlowConstants.CLIENT_ID.getValue()))
                .orElseThrow(() -> new ResourceNotFoundException("application not found for client_id: " + params.get(AuthorizationCodeFlowConstants.CLIENT_ID.getValue())));

        if (attributeValidator.isNotBlank(params.getOrDefault(AuthorizationCodeFlowConstants.REDIRECT_URI.getValue(), ""))) {
            checkArgument(attributeValidator.equals(application.getRedirectUri(), params.get(AuthorizationCodeFlowConstants.REDIRECT_URI.getValue())), "redirect_url is not valid");
        }
        // after this point we need to redirect all errors back to the client
        final var redirectUri = requestParameterValidator.validateAndOptionallyRedirect(application.getRedirectUri(), params);
        if (attributeValidator.isNotBlank(redirectUri)) {
            return redirectUri;
        }

        final var identity = authentication.getIdentity();
        identity.getAuthorizedApplications().add(application);

        // todo: think about what to do if there is an authorization code already present for the user
        final var authCode = authCodeInstantiator.instantiate(identity, params);

        return redirectUrlGenerator.generate(
                application.getRedirectUri(),
                successParamGenerator.generate(params, authCodeRepository.save(authCode).getAuthorizationCode()));
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public AccessTokenResponse exchange(final HttpServletRequest servletRequest, final Map<String, String> params) {

        if (attributeValidator.isBlank(params.getOrDefault(AuthorizationCodeFlowConstants.GRANT_TYPE.getValue(), ""))) {
            throw  new BadRequestException("grant_type must not be blank");
        }

        if (attributeValidator.equals(params.get(AuthorizationCodeFlowConstants.GRANT_TYPE.getValue()), "client_credentials")) {
            // this is resource to resource authorization
            // first I need to check for params
            boolean isNotAuthorized = true;
            if (attributeValidator.isNotBlank(params.get(AuthorizationCodeFlowConstants.CLIENT_ID.getValue()))) {
                final var application = applicationRepository.findByClientId(params.get(AuthorizationCodeFlowConstants.CLIENT_ID.getValue()))
                        .orElseThrow(() -> new BadRequestException("application not found for id: " + params.get(AuthorizationCodeFlowConstants.CLIENT_ID.getValue())));
                final var clientSecret = params.getOrDefault(AuthorizationCodeFlowConstants.CLIENT_SECRET.getValue(), "");
                final var secrets = application.getClientSecrets().stream().map(ClientSecretEntity::getSecretKey).collect(Collectors.toList());

                if (attributeValidator.isNotBlank(clientSecret)) {
                    for (String secret : secrets) {
                        if (secret.equals(clientSecret)) {
                            isNotAuthorized = false;
                            break;
                        }
                    }
                }
            }


            if (isNotAuthorized) {
                // check for http basic
                final var basicAuthHeaderValue = tokenExtractor.extractBasicAuthToken(servletRequest);

                if (attributeValidator.isNotBlank(basicAuthHeaderValue)) {
                    final var decodedValuesBytes = Base64.getDecoder().decode(basicAuthHeaderValue);
                    final var decodedValuesStr = new String(decodedValuesBytes);
                    final var decodedValues = decodedValuesStr.split(":");


                    if (decodedValues.length != 2) {
                        return AccessTokenResponse.withError("");
                    }
                    final var application = applicationRepository.findByClientId(decodedValues[0])
                            .orElseThrow(() -> new BadRequestException("application not found for id: " + decodedValues[0]));
                    final var secrets = application.getClientSecrets().stream().map(ClientSecretEntity::getSecretKey).collect(Collectors.toList());


                    final var clientId = application.getClientId();
                    if (attributeValidator.doesNotEqual(clientId, decodedValues[0])) {
                        return AccessTokenResponse.withError("");
                    }
                    final var encodedSubmittedSecret = encoder.encode(decodedValues[1]);
                    for (String secret: secrets) {

                        if (encoder.matches(decodedValues[1], secret)) {
                            isNotAuthorized = false;
                            break;
                        }
                    }
                    if (isNotAuthorized) {
                        return AccessTokenResponse.withError("redirectUri");
                    }
                    return accessTokenResponseMapper
                            .map(accessTokenRepository
                                    .save(accessTokenInstantiator
                                            .instantiate(application.getId())));
                }
            }
        }

        if (attributeValidator.equals(params.get(AuthorizationCodeFlowConstants.GRANT_TYPE.getValue()), AuthorizationCodeFlowConstants.AUTHORIZATION_CODE_GRANT_TYPE.getValue())) {

            if (attributeValidator.isBlank(params.getOrDefault(AuthorizationCodeFlowConstants.CLIENT_ID.getValue(), ""))) {
                throw new BadRequestException("application id blank or malformed");
            }

            final var application = applicationRepository.findByClientId(params.get(AuthorizationCodeFlowConstants.CLIENT_ID.getValue()))
                    .orElseThrow(() -> new BadRequestException("application not found for id: " + params.get(AuthorizationCodeFlowConstants.CLIENT_ID.getValue())));

            applicationAccessTokenValidator.validate(application, params);


            final var redirectUri = accessTokenRequestParameterValidator.validateAndOptionallyRedirect(application, params);
            if (attributeValidator.isNotBlank(redirectUri)) {
                return AccessTokenResponse.withError(redirectUri);
            }

            final var inProgressExternalAuthorizationOpt = inProgressAuthRepository.findByState(params.getOrDefault(AuthorizationCodeFlowConstants.STATE.getValue(), ""));

            if (inProgressExternalAuthorizationOpt.isPresent()) {
                // this is an external authorization
                // need to create user on the fly if they don't exist yet
                final var externalInProgressAuth = inProgressExternalAuthorizationOpt.get();
                final var provider = externalInProgressAuth.getProvider();
                final var identityResponse = completeAuthorizationWithProvider(
                        params.get(AuthorizationCodeFlowConstants.AUTHORIZATION_CODE.getValue()),
                        provider.getName(), params.get(AuthorizationCodeFlowConstants.CLIENT_ID.getValue()),
                        params.get(AuthorizationCodeFlowConstants.STATE.getValue()));
                inProgressAuthRepository.delete(inProgressExternalAuthorizationOpt.get());
                return accessTokenResponseMapper
                        .map(accessTokenRepository
                                .save(accessTokenInstantiator
                                        .instantiate(identityResponse.getId())));
            }

            // check authorization code
            final var authorizationCode = authCodeRepository
                    .findByAuthorizationCodeAndActiveTrue(params.get(AuthorizationCodeFlowConstants.AUTHORIZATION_CODE.getValue()))
                    .orElseThrow(() -> new BadRequestException("invalid authorization code"));

            // now that authorization code has been used it needs to be set inactive to prevent reuse
            // todo: if an access code is used more than once we should consider it fraudulent
            authorizationCode.setActive(false);

            final var codeVerifier = params.get(AuthorizationCodeFlowConstants.CODE_VERIFIER.getValue());
            // check code_verifier / pkce
            if (authorizationCode.getCodeChallengeMethod().equals(CodeChallengeMethod.S256)) {
                // todo UrlEncoding is adding == for some reason.  Not sure.  Need to look into this.
                final String otherOutStr = Base64
                        .getUrlEncoder()
                        .encodeToString(sha256Hasher.hash(codeVerifier).getBytes(StandardCharsets.UTF_8))
                        .replace("==", "");
                if (attributeValidator.equals(otherOutStr, authorizationCode.getCodeChallenge())) {
                    return accessTokenResponseMapper
                            .map(accessTokenRepository
                                    .save(accessTokenInstantiator
                                            .instantiate(authorizationCode.getIdentityId())));
                } else {
                    // todo potentially need to redirect
                    return null;
                }
            }

            if (attributeValidator.equals(codeVerifier, authorizationCode.getCodeChallenge())) {
                final var accessToken = accessTokenRepository
                        .save(accessTokenInstantiator
                                .instantiate(authorizationCode.getIdentityId()));

                final var refreshToken = refreshTokenInstantiator.instantiate(accessToken.getAccessToken());
                accessToken.setRefreshToken(refreshToken);
                refreshToken.setAccessToken(accessToken);

                return accessTokenResponseMapper.map(accessToken);
            } else {
                // potentially need to redirect
                return null;
            }

        }

        return AccessTokenResponse.withError("Not Authorized");

    }

    @Transactional(propagation = Propagation.REQUIRED)
    public String proxyAuthorizationRequestToProvider(final String responseType, final String state, final String redirectUri, final String clientId, final String providerName) {
        checkArgument(attributeValidator.equals(responseType, "code"));
        checkArgument(attributeValidator.isNotBlank(state));
        checkArgument(attributeValidator.isNotBlank(redirectUri));
        checkArgument(attributeValidator.isNotBlank(clientId));
        checkArgument(attributeValidator.isNotBlank(providerName));

        final var application = applicationRepository.findByClientId(clientId)
                .orElseThrow(() -> new ResourceNotFoundException("application not found for clientId: " + clientId));

        final var tenant = tenantRepository.findById(application.getTenantId())
                .orElseThrow(() -> new ResourceNotFoundException(String.format("tenant %s not found with application: %s", application.getTenantId(), clientId)));

        var providerFound = false;
        ExternalIdentityProviderEntity provider = null;
        for (ExternalIdentityProviderEntity providerEntity: tenant.getExternalIdentityProviders()) {
            if (providerEntity.getName().equalsIgnoreCase(providerName) && providerEntity.isActive()) {
                providerFound = true;
                provider = providerEntity;
            }
        }

        if (providerFound) {

            final var inProgressAuth = inProgressAuthRepository
                    .save(inProgressAuthInstantiator.instantiate(provider, state, redirectUri, clientId));

            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

            params.add("scope", provider.getScope());
            params.add("client_id", provider.getClientId());
            params.add("state", state);

            return redirectUrlGenerator.generate(provider.getBaseAuthorizationUrl(), params);

        }
        // todo: throw exception for provider not found
        return null;
    }
}
