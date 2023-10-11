/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package software.iridium.api.service;

import static com.google.common.base.Preconditions.checkArgument;

import jakarta.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Calendar;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import software.iridium.api.authentication.client.ProviderAccessTokenRequestor;
import software.iridium.api.authentication.domain.AccessTokenResponse;
import software.iridium.api.authentication.domain.ApplicationAuthorizationFormRequest;
import software.iridium.api.authentication.domain.CodeChallengeMethod;
import software.iridium.api.authentication.domain.IdentityResponse;
import software.iridium.api.base.error.BadRequestException;
import software.iridium.api.base.error.ClientAuthenticationException;
import software.iridium.api.base.error.NotAuthorizedException;
import software.iridium.api.base.error.ResourceNotFoundException;
import software.iridium.api.fetcher.ExternalProviderUserProfileFetcher;
import software.iridium.api.generator.ExternalProviderAccessTokenUrlGenerator;
import software.iridium.api.generator.RedirectUrlGenerator;
import software.iridium.api.generator.SuccessAuthorizationParameterGenerator;
import software.iridium.api.instantiator.*;
import software.iridium.api.mapper.AccessTokenResponseMapper;
import software.iridium.api.mapper.IdentityResponseMapper;
import software.iridium.api.repository.*;
import software.iridium.api.util.AttributeValidator;
import software.iridium.api.util.AuthorizationCodeFlowConstants;
import software.iridium.api.util.SHA256Hasher;
import software.iridium.api.util.ServletTokenExtractor;
import software.iridium.api.util.SubdomainExtractor;
import software.iridium.api.validator.*;
import software.iridium.entity.ApplicationType;
import software.iridium.entity.ClientSecretEntity;
import software.iridium.entity.ExternalIdentityProviderEntity;

@Service
public class AuthorizationService {

  // todo may need to break this out into smaller classes
  @Autowired private ExternalProviderAccessTokenUrlGenerator externalAccessTokenUrlGenerator;
  @Autowired private ProviderAccessTokenRequestor accessTokenRequestor;
  @Autowired private ExternalProviderUserProfileFetcher externalProfileFetcher;
  @Autowired private IdentityEntityInstantiator identityInstantiator;
  @Autowired private IdentityEntityRepository identityRepository;
  @Autowired private IdentityEmailEntityRepository emailRepository;
  @Autowired private IdentityResponseMapper identityResponseMapper;
  @Autowired private AuthorizationGrantTypeParamValidator grantTypeValidator;
  @Autowired private ApplicationEntityRepository applicationRepository;
  @Autowired private RedirectUrlGenerator redirectUrlGenerator;
  @Autowired private AttributeValidator attributeValidator;
  @Autowired private AuthorizationCodeEntityInstantiator authCodeInstantiator;
  @Autowired private AuthorizationCodeEntityRepository authCodeRepository;
  @Autowired private AuthorizationRequestParameterValidator requestParameterValidator;
  @Autowired private SuccessAuthorizationParameterGenerator successParamGenerator;
  @Autowired private AccessTokenRequestParameterValidator accessTokenRequestParameterValidator;
  @Autowired private ApplicationEntityAccessTokenRequestValidator applicationAccessTokenValidator;
  @Autowired private SHA256Hasher sha256Hasher;
  @Autowired private AccessTokenEntityInstantiator accessTokenInstantiator;
  @Autowired private AccessTokenEntityRepository accessTokenRepository;
  @Autowired private AccessTokenResponseMapper accessTokenResponseMapper;
  @Autowired private SubdomainExtractor subdomainExtractor;
  @Autowired private TenantEntityRepository tenantRepository;
  @Autowired private AuthenticationEntityRepository authenticationRepository;

  @Autowired
  private InProgressExternalIdentityProviderAuthorizationInstantiator inProgressAuthInstantiator;

  @Autowired
  private InProgressExternalIdentityProviderAuthorizationEntityRepository inProgressAuthRepository;

  @Autowired private ServletTokenExtractor tokenExtractor;
  @Autowired private BCryptPasswordEncoder encoder;
  @Autowired private RefreshTokenEntityRepository refreshTokenRepository;
  @Autowired private RefreshTokenRequestValidator refreshTokenRequestValidator;
  @Autowired private ApplicationService applicationService;

  @Transactional(propagation = Propagation.REQUIRED)
  public IdentityResponse completeAuthorizationWithProvider(
      final String code, final String providerName, final String clientId, final String state) {
    checkArgument(attributeValidator.isNotBlank(code), "code must be not be blank");
    checkArgument(attributeValidator.isNotBlank(providerName), "providerName must be blank");
    checkArgument(
        attributeValidator.isNotBlankAndNoLongerThan(clientId, 32),
        "clientId must not be blank and no longer than 32 characters");
    checkArgument(attributeValidator.isNotBlank(state), "state must be not be blank");

    final var application = applicationService.findByClientId(clientId, "");
    final var tenant =
        tenantRepository
            .findById(application.getTenantId())
            .orElseThrow(
                () ->
                    new ResourceNotFoundException(
                        String.format(
                            "tenant %s not found with clientId: %s",
                            application.getTenantId(), clientId)));

    var providerFound = false;
    ExternalIdentityProviderEntity provider = null;
    for (ExternalIdentityProviderEntity providerEntity : tenant.getExternalIdentityProviders()) {
      if (providerEntity.getName().equalsIgnoreCase(providerName) && providerEntity.isActive()) {
        providerFound = true;
        provider = providerEntity;
      }
    }

    if (providerFound) {
      final var providerUrl = externalAccessTokenUrlGenerator.generate(provider, application, code);

      final var response = accessTokenRequestor.requestAccessToken(providerUrl);

      final var externalProfile = externalProfileFetcher.fetch(provider, response);

      final var emailOptional =
          emailRepository.findByEmailAddressAndIdentity_ParentTenantIdAndIdentity_Provider_Id(
              externalProfile.getEmail(), tenant.getId(), provider.getId());
      if (emailOptional.isEmpty()) {

        final var identity = identityInstantiator.instantiate(externalProfile, provider);
        identity.getAuthorizedApplications().add(application);
        identity.setParentTenantId(tenant.getId());
        return identityResponseMapper.map(identityRepository.save(identity));
      }
      // todo: add logic for continuous property sync for external providers
      // todo: record login timestamps
      return identityResponseMapper.map(emailOptional.get().getIdentity());
    }
    throw new ResourceNotFoundException(
        String.format(
            "active provider: %s not found for tenant: %s", providerName, tenant.getId()));
  }

  @Transactional(propagation = Propagation.REQUIRED)
  public String authorize(
      final ApplicationAuthorizationFormRequest formRequest,
      final Map<String, String> params,
      final HttpServletRequest servletRequest) {
    grantTypeValidator.validate(params);

    final var subdomain = subdomainExtractor.extract(servletRequest.getRequestURL().toString());

    tenantRepository
        .findBySubdomain(subdomain)
        .orElseThrow(
            () -> new ResourceNotFoundException("tenant not found for subdomain " + subdomain));

    final var authentication =
        authenticationRepository
            .findByAuthTokenAndExpirationAfter(
                formRequest.getUserToken(), Calendar.getInstance().getTime())
            .orElseThrow(NotAuthorizedException::new);

    final var application =
        applicationRepository
            .findByClientId(params.get(AuthorizationCodeFlowConstants.CLIENT_ID.getValue()))
            .orElseThrow(
                () ->
                    new ResourceNotFoundException(
                        "application not found for client_id: "
                            + params.get(AuthorizationCodeFlowConstants.CLIENT_ID.getValue())));

    if (attributeValidator.isNotBlank(
        params.getOrDefault(AuthorizationCodeFlowConstants.REDIRECT_URI.getValue(), ""))) {
      checkArgument(
          attributeValidator.equals(
              application.getRedirectUri(),
              params.get(AuthorizationCodeFlowConstants.REDIRECT_URI.getValue())),
          "redirect_url is not valid");
    }
    // after this point we need to redirect all errors back to the client
    final var redirectUri =
        requestParameterValidator.validateAndOptionallyRedirect(
            application.getRedirectUri(), params);
    if (attributeValidator.isNotBlank(redirectUri)) {
      return redirectUri;
    }

    final var identity = authentication.getIdentity();
    identity.getAuthorizedApplications().add(application);

    // todo: think about what to do if there is an authorization code already present for the user
    final var authCode = authCodeInstantiator.instantiate(identity, params);

    return redirectUrlGenerator.generate(
        application.getRedirectUri(),
        successParamGenerator.generate(
            params, authCodeRepository.save(authCode).getAuthorizationCode()));
  }

  @Transactional(propagation = Propagation.REQUIRED)
  public AccessTokenResponse exchange(
      final HttpServletRequest servletRequest, final Map<String, String> params) {

    if (attributeValidator.isBlank(
        params.getOrDefault(AuthorizationCodeFlowConstants.GRANT_TYPE.getValue(), ""))) {
      throw new BadRequestException("grant_type must not be blank");
    }

    if (attributeValidator.equals(
        params.get(AuthorizationCodeFlowConstants.GRANT_TYPE.getValue()), "client_credentials")) {
      // this is resource to resource authorization
      // first I need to check for params
      boolean isNotAuthorized = true;
      if (attributeValidator.isNotBlank(
          params.get(AuthorizationCodeFlowConstants.CLIENT_ID.getValue()))) {
        final var application =
            applicationRepository
                .findByClientId(params.get(AuthorizationCodeFlowConstants.CLIENT_ID.getValue()))
                .orElseThrow(() -> new BadRequestException("invalid request"));
        final var clientSecret =
            params.getOrDefault(AuthorizationCodeFlowConstants.CLIENT_SECRET.getValue(), "");
        final var secrets =
            application.getClientSecrets().stream()
                .map(ClientSecretEntity::getSecretKey)
                .collect(Collectors.toList());

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
            throw new BadRequestException("client credential request invalid format");
          }
          final var application =
              applicationRepository
                  .findByClientId(decodedValues[0])
                  .orElseThrow(() -> new BadRequestException("invalid_client"));
          final var secrets =
              application.getClientSecrets().stream()
                  .map(ClientSecretEntity::getSecretKey)
                  .collect(Collectors.toList());

          final var clientId = application.getClientId();
          if (attributeValidator.doesNotEqual(clientId, decodedValues[0])) {
            throw new ClientAuthenticationException("invalid_client");
          }
          final var encodedSubmittedSecret = encoder.encode(decodedValues[1]);
          for (String secret : secrets) {

            if (encoder.matches(decodedValues[1], secret)) {
              isNotAuthorized = false;
              break;
            }
          }
          if (isNotAuthorized) {
            throw new ClientAuthenticationException("invalid_client");
          }
          return accessTokenResponseMapper.map(
              accessTokenRepository.save(accessTokenInstantiator.instantiate(application.getId())));
        }
      }
    }

    if (attributeValidator.equals(
        params.get(AuthorizationCodeFlowConstants.GRANT_TYPE.getValue()),
        AuthorizationCodeFlowConstants.AUTHORIZATION_CODE_GRANT_TYPE.getValue())) {

      if (attributeValidator.isBlank(
          params.getOrDefault(AuthorizationCodeFlowConstants.CLIENT_ID.getValue(), ""))) {
        throw new BadRequestException("invalid_request");
      }

      final var application =
          applicationRepository
              .findByClientId(params.get(AuthorizationCodeFlowConstants.CLIENT_ID.getValue()))
              .orElseThrow(() -> new BadRequestException("invalid request"));

      applicationAccessTokenValidator.validate(application, params);

      final var redirectUri =
          accessTokenRequestParameterValidator.validateAndOptionallyRedirect(application, params);
      if (attributeValidator.isNotBlank(redirectUri)) {
        throw new BadRequestException("invalid request");
      }

      final var inProgressExternalAuthorizationOpt =
          inProgressAuthRepository.findByState(
              params.getOrDefault(AuthorizationCodeFlowConstants.STATE.getValue(), ""));

      if (inProgressExternalAuthorizationOpt.isPresent()) {
        // this is an external authorization
        // need to create user on the fly if they don't exist yet
        final var externalInProgressAuth = inProgressExternalAuthorizationOpt.get();
        final var provider = externalInProgressAuth.getProvider();
        final var identityResponse =
            completeAuthorizationWithProvider(
                params.get(AuthorizationCodeFlowConstants.AUTHORIZATION_CODE.getValue()),
                provider.getName(),
                params.get(AuthorizationCodeFlowConstants.CLIENT_ID.getValue()),
                params.get(AuthorizationCodeFlowConstants.STATE.getValue()));
        inProgressAuthRepository.delete(inProgressExternalAuthorizationOpt.get());
        return accessTokenResponseMapper.map(
            accessTokenRepository.save(
                accessTokenInstantiator.instantiate(identityResponse.getId())));
      }

      // check authorization code
      final var authorizationCode =
          authCodeRepository
              .findByAuthorizationCodeAndActiveTrue(
                  params.get(AuthorizationCodeFlowConstants.AUTHORIZATION_CODE.getValue()))
              .orElseThrow(() -> new BadRequestException("invalid authorization code"));

      // now that authorization code has been used it needs to be set inactive to prevent reuse
      // todo: if an access code is used more than once we should consider it fraudulent
      authorizationCode.setActive(false);

      final var codeVerifier = params.get(AuthorizationCodeFlowConstants.CODE_VERIFIER.getValue());
      // check code_verifier / pkce
      if (authorizationCode.getCodeChallengeMethod().equals(CodeChallengeMethod.S256)) {
        // todo UrlEncoding is adding ==.  Need to address at some point.
        final String otherOutStr =
            Base64.getUrlEncoder()
                .encodeToString(sha256Hasher.hash(codeVerifier).getBytes(StandardCharsets.UTF_8))
                .replace("==", "");
        if (attributeValidator.equals(otherOutStr, authorizationCode.getCodeChallenge())) {
          return accessTokenResponseMapper.map(
              accessTokenRepository.save(
                  accessTokenInstantiator.instantiate(authorizationCode.getIdentityId())));
        } else {
          // todo potentially need to redirect
          return null;
        }
      }

      if (attributeValidator.equals(codeVerifier, authorizationCode.getCodeChallenge())) {
        final var accessToken =
            accessTokenRepository.save(
                accessTokenInstantiator.instantiate(authorizationCode.getIdentityId()));

        return accessTokenResponseMapper.map(accessToken);
      } else {
        // potentially need to redirect
        return null;
      }
    }

    throw new BadRequestException("invalid_grant");
  }

  @Transactional(propagation = Propagation.REQUIRED)
  public String proxyAuthorizationRequestToProvider(
      final String responseType,
      final String state,
      final String redirectUri,
      final String clientId,
      final String providerName) {
    checkArgument(attributeValidator.equals(responseType, "code"));
    checkArgument(attributeValidator.isNotBlank(state));
    checkArgument(attributeValidator.isNotBlank(redirectUri));
    checkArgument(attributeValidator.isNotBlank(clientId));
    checkArgument(attributeValidator.isNotBlank(providerName));

    final var application =
        applicationRepository
            .findByClientId(clientId)
            .orElseThrow(
                () ->
                    new ResourceNotFoundException(
                        "application not found for clientId: " + clientId));

    final var tenant =
        tenantRepository
            .findById(application.getTenantId())
            .orElseThrow(
                () ->
                    new ResourceNotFoundException(
                        String.format(
                            "tenant %s not found with application: %s",
                            application.getTenantId(), clientId)));

    var providerFound = false;
    ExternalIdentityProviderEntity provider = null;
    for (ExternalIdentityProviderEntity providerEntity : tenant.getExternalIdentityProviders()) {
      if (providerEntity.getName().equalsIgnoreCase(providerName) && providerEntity.isActive()) {
        providerFound = true;
        provider = providerEntity;
      }
    }

    if (providerFound) {

      inProgressAuthRepository.save(
          inProgressAuthInstantiator.instantiate(provider, state, redirectUri, clientId));

      return redirectUrlGenerator.generate(
          provider.getBaseAuthorizationUrl(), application, provider, state);
    }
    // todo: throw exception for provider not found
    return null;
  }

  @Transactional(propagation = Propagation.REQUIRED)
  public AccessTokenResponse refreshToken(String grantType, String clientId, String refreshToken) {
    refreshTokenRequestValidator.validate(grantType, clientId, refreshToken);

    final var application =
        applicationRepository
            .findByClientId(clientId)
            .orElseThrow(
                () -> new BadRequestException("application not found for clientId: " + clientId));

    // Check if the client is confidential
    if (application.getApplicationType().getType().equals(ApplicationType.MOBILE_OR_NATIVE)
        || application.getApplicationType().getType().equals(ApplicationType.SINGLE_PAGE)) {
      throw new NotAuthorizedException("Only confidential clients are allowed to refresh tokens.");
    }

    refreshTokenRepository
        .findByRefreshToken(refreshToken)
        .orElseThrow(() -> new BadRequestException("invalid refresh token: " + refreshToken));

    final var accessToken =
        accessTokenRepository.save(accessTokenInstantiator.instantiate(application.getId()));
    return accessTokenResponseMapper.map(accessToken);
  }
}
