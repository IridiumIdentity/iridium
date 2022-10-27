/*
 *  Copyright (C) Josh Fischer - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Josh Fischer <josh@joshfischer.io>, 2022.
 */

package software.iridium.api.service;

import software.iridium.api.base.error.DuplicateResourceException;
import software.iridium.api.base.error.NotAuthorizedException;
import software.iridium.api.base.error.ResourceNotFoundException;
import software.iridium.api.authentication.domain.AuthenticationRequest;
import software.iridium.api.authentication.domain.AuthenticationResponse;
import software.iridium.api.authentication.domain.CreateIdentityRequest;
import software.iridium.api.authentication.domain.IdentityResponse;
import software.iridium.api.entity.*;
import software.iridium.api.handler.NewIdentityEventHandler;
import software.iridium.api.instantiator.AuthenticationRequestInstantiator;
import software.iridium.api.instantiator.IdentityCreateRequestDetailsInstantiator;
import software.iridium.api.instantiator.IdentityEntityInstantiator;
import software.iridium.api.mapper.IdentityEntityMapper;
import software.iridium.api.mapper.IdentityResponseMapper;
import software.iridium.api.repository.*;
import software.iridium.api.util.ServletTokenExtractor;
import software.iridium.api.util.AttributeValidator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class IdentityServiceTest {

    @Mock
    private AuthenticationEntityRepository mockAuthenticationEntityRepository;
    @Mock
    private IdentityEntityMapper mockIdentityEntityMapper;
    @Mock
    private IdentityEntityInstantiator mockIdentityInstantiator;
    @Mock
    private IdentityEntityRepository mockIdentityRepository;
    @Mock
    private IdentityResponseMapper mockResponseMapper;
    @Mock
    private BCryptPasswordEncoder mockEncoder;
    @Mock
    private NewIdentityEventHandler mockEventHandler;
    @Mock
    private IdentityEmailEntityRepository mockEmailRepository;
    @Mock
    private HttpServletRequest mockServletRequest;
    @Mock
    private ServletTokenExtractor mockTokenExtractor;
    @Mock
    private AttributeValidator mockAttributeValidator;
    @Mock
    private TenantEntityRepository mockTenantRepository;
    @Mock
    private ApplicationEntityRepository mockApplicationRepository;
    @Mock
    private AuthenticationRequestInstantiator mockRequestInstantiator;
    @Mock
    private AuthenticationService mockAuthenticationService;
    @Mock
    private IdentityCreateRequestDetailsInstantiator mockRequestDetailsInstantiator;
    @InjectMocks
    private IdentityService subject;

    @AfterEach
    public void ensureNoUnexpectedMockInteractions() {
        Mockito.verifyNoMoreInteractions(
                mockAuthenticationEntityRepository,
                mockIdentityEntityMapper,
                mockIdentityInstantiator,
                mockIdentityRepository,
                mockResponseMapper,
                mockEncoder,
                mockEventHandler,
                mockServletRequest,
                mockTokenExtractor,
                mockAttributeValidator,
                mockTenantRepository,
                mockApplicationRepository,
                mockRequestInstantiator,
                mockAuthenticationService,
                mockRequestDetailsInstantiator
        );
    }

    @Test
    public void getIdentity_AllGood_BehavesAsExpected() {
        final var userAuthToken = "userAuthToken";
        final var auth = new AuthenticationEntity();
        final var identity = new IdentityEntity();
        auth.setIdentity(identity);
        final var identityResponse = new IdentityResponse();

        when(mockAuthenticationEntityRepository
                .findFirstByAuthTokenAndExpirationAfter(same(userAuthToken), any(Date.class)))
                .thenReturn(Optional.of(auth));
        when(mockIdentityEntityMapper.map(same(identity))).thenReturn(identityResponse);
        when(mockTokenExtractor.extractIridiumToken(same(mockServletRequest))).thenReturn(userAuthToken);

        assertThat(subject.getIdentity(mockServletRequest), sameInstance(identityResponse));

        verify(mockAuthenticationEntityRepository)
                .findFirstByAuthTokenAndExpirationAfter(same(userAuthToken), any(Date.class));
        verify(mockIdentityEntityMapper).map(same(identity));
        verify(mockTokenExtractor).extractIridiumToken(same(mockServletRequest));
    }

    @Test
    public void getIdentity_EntityNotFound_ExceptionThrown() {
        final var userAuthToken = "userAuthToken";

        when(mockAuthenticationEntityRepository
                .findFirstByAuthTokenAndExpirationAfter(same(userAuthToken), any(Date.class)))
                .thenReturn(Optional.empty());
        when(mockTokenExtractor.extractIridiumToken(same(mockServletRequest))).thenReturn(userAuthToken);


        assertThrows(NotAuthorizedException.class, () -> subject.getIdentity(mockServletRequest));

        verify(mockAuthenticationEntityRepository)
                .findFirstByAuthTokenAndExpirationAfter(same(userAuthToken), any(Date.class));
        verify(mockIdentityEntityMapper, never()).map(any());
        verify(mockTokenExtractor).extractIridiumToken(same(mockServletRequest));
    }

    @Test
    public void create_AllGood_BehavesAsExpected() {
        final var emailAddress = "you@nowehere.com";
        final var clientId = "theClientId";
        final var password = "the password";
        final var request = new CreateIdentityRequest();
        request.setUsername(emailAddress);
        request.setClientId(clientId);
        request.setPassword(password);
        final var entity = new IdentityEntity();
        final var encodedTempPassword = "ec0d3dm3";
        final var response = new IdentityResponse();
        final var tenantId = "the tenantId";
        final var tenant = new TenantEntity();

        final var authenticationRequest = new AuthenticationRequest();
        final var requestParams = new HashMap<String, String>();
        final var authenticationResponse = new AuthenticationResponse();
        final var application = new ApplicationEntity();
        application.setClientId(clientId);
        application.setTenantId(tenantId);
        final var sessionDetails = new IdentityCreateSessionDetails();

        when(mockEncoder.encode(same(password))).thenReturn(encodedTempPassword);
        when(mockIdentityInstantiator.instantiate(same(request), same(encodedTempPassword), same(tenantId))).thenReturn(entity);
        when(mockIdentityRepository.save(entity)).thenReturn(entity);
        when(mockResponseMapper.map(same(entity), same(authenticationResponse))).thenReturn(response);
        when(mockAttributeValidator.isNotBlank(same(clientId))).thenReturn(true);
        when(mockTenantRepository.findById(same(tenantId))).thenReturn(Optional.of(tenant));
        when(mockRequestInstantiator.instantiate(same(request))).thenReturn(authenticationRequest);
        when(mockAuthenticationService.authenticate(same(authenticationRequest), same(requestParams))).thenReturn(authenticationResponse);
        when(mockApplicationRepository.findByClientId(same(clientId))).thenReturn(Optional.of(application));
        when(mockEmailRepository.findByEmailAddressAndIdentity_ParentTenantId(same(emailAddress), same(tenantId))).thenReturn(Optional.empty());
        when(mockRequestDetailsInstantiator.instantiate(same(requestParams), same(entity))).thenReturn(sessionDetails);

        subject.create(request, requestParams);

        verify(mockEmailRepository).findByEmailAddressAndIdentity_ParentTenantId(same(emailAddress), same(tenantId));
        verify(mockEncoder).encode(same(password));
        verify(mockIdentityInstantiator).instantiate(same(request), same(encodedTempPassword), same(tenantId));
        verify(mockIdentityRepository).save(same(entity));
        verify(mockResponseMapper).map(same(entity), same(authenticationResponse));
        verify(mockEventHandler).handleEvent(same(entity), same(clientId));
        verify(mockAttributeValidator).isNotBlank(same(clientId));
        verify(mockTenantRepository).findById(same(tenantId));
        verify(mockRequestInstantiator).instantiate(same(request));
        verify(mockAuthenticationService).authenticate(same(authenticationRequest), same(requestParams));
        verify(mockEmailRepository).findByEmailAddressAndIdentity_ParentTenantId(same(emailAddress), same(tenantId));
        verify(mockRequestDetailsInstantiator).instantiate(same(requestParams), same(entity));

    }

    @Test
    public void create_DuplicateUser_ExceptionThrown() {
        final var emailAddress = "you@nowehere.com";
        final var clientId = "theClientId";
        final var password = "the password";
        final var request = new CreateIdentityRequest();
        request.setUsername(emailAddress);
        request.setClientId(clientId);
        request.setPassword(password);
        final var tenantId = "the tenantId";
        final var tenant = new TenantEntity();
        final var application = new ApplicationEntity();
        final var requestParams = new HashMap<String, String>();
        application.setTenantId(tenantId);
        final var email = new IdentityEmailEntity();

        when(mockAttributeValidator.isNotBlank(same(clientId))).thenReturn(true);
        when(mockTenantRepository.findById(same(tenantId))).thenReturn(Optional.of(tenant));
        when(mockApplicationRepository.findByClientId(same(clientId))).thenReturn(Optional.of(application));
        when(mockEmailRepository.findByEmailAddressAndIdentity_ParentTenantId(same(emailAddress), same(tenantId))).thenReturn(Optional.of(email));

        final var exception = assertThrows(DuplicateResourceException.class, () -> subject.create(request, requestParams));

        verify(mockAttributeValidator).isNotBlank(same(clientId));
        verify(mockTenantRepository).findById(same(tenantId));
        verify(mockApplicationRepository).findByClientId(same(clientId));
        verify(mockEmailRepository).findByEmailAddressAndIdentity_ParentTenantId(same(emailAddress), same(tenantId));

        assertThat(exception.getMessage(), is(equalTo("Account already registered with: you@nowehere.com in tenant: " + tenantId)));
    }

    @Test
    public void create_ApplicationNotfoundByClientId_ExceptionThrown() {
        final var emailAddress = "you@nowehere.com";
        final var clientId = "the-client-id";
        final var request = new CreateIdentityRequest();
        request.setUsername(emailAddress);
        request.setClientId(clientId);
        final var entity = new IdentityEntity();
        final var emailEntity = new IdentityEmailEntity();
        emailEntity.setIdentity(entity);

        final Map paramMap = new HashMap<String, String>();

        when(mockAttributeValidator.isNotBlank(same(clientId))).thenReturn(true);
        when(mockApplicationRepository.findByClientId(same(clientId))).thenReturn(Optional.empty());

        final var exception = assertThrows(ResourceNotFoundException.class, () -> subject.create(request, paramMap));

        assertThat(exception.getMessage(), is(equalTo("application not found for clientId: " + clientId)));
        verify(mockAttributeValidator).isNotBlank(same(clientId));
        verify(mockApplicationRepository).findByClientId(same(clientId));
    }

    @Test
    public void create_TenantNotFound_ExceptionThrown() {
        final var emailAddress = "you@nowehere.com";
        final var clientId = "the-client-id";
        final var request = new CreateIdentityRequest();
        request.setUsername(emailAddress);
        request.setClientId(clientId);
        final var entity = new IdentityEntity();
        final var emailEntity = new IdentityEmailEntity();
        emailEntity.setIdentity(entity);
        final var application = new ApplicationEntity();
        final var tenantId = "the-tenantId";
        application.setTenantId(tenantId);

        final Map paramMap = new HashMap<String, String>();

        when(mockAttributeValidator.isNotBlank(same(clientId))).thenReturn(true);
        when(mockApplicationRepository.findByClientId(same(clientId))).thenReturn(Optional.of(application));
        when(mockTenantRepository.findById(same(tenantId))).thenReturn(Optional.empty());

        final var exception = assertThrows(ResourceNotFoundException.class, () -> subject.create(request, paramMap));

        assertThat(exception.getMessage(), is(equalTo("tenant not found for id: " + tenantId)));
        verify(mockAttributeValidator).isNotBlank(same(clientId));
        verify(mockApplicationRepository).findByClientId(same(clientId));
        verify(mockTenantRepository).findById(same(tenantId));
    }

    @Test
    public void create_ImproperlyFormattedEmail_ExceptionThrown() {
        final var emailAddress = "you@@@@nowehere.com";
        final var request = new CreateIdentityRequest();
        request.setUsername(emailAddress);
        final var entity = new IdentityEntity();
        final var emailEntity = new IdentityEmailEntity();
        emailEntity.setIdentity(entity);
        final var tenantId = "the-tenantId";
        // todo
        //final var exception = assertThrows(IllegalArgumentException.class, () -> subject.create(request));

        //assertThat(exception.getMessage(), is(equalTo("email must not be blank and properly formatted: you@@@@nowehere.com")));
    }
}
