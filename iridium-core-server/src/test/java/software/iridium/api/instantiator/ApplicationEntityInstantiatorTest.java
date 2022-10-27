/*
 *  Copyright (C) Josh Fischer - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Josh Fischer <josh@joshfischer.io>, 2022.
 */

package software.iridium.api.instantiator;

import org.hamcrest.MatcherAssert;
import software.iridium.api.authentication.domain.ApplicationCreateRequest;
import software.iridium.api.entity.ApplicationTypeEntity;
import software.iridium.api.util.EncoderUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.security.NoSuchAlgorithmException;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ApplicationEntityInstantiatorTest {

    @Mock
    private EncoderUtils mockEncoderUtils;
    @InjectMocks
    private  ApplicationEntityInstantiator subject;

    @AfterEach
    public void ensureNoUnexpectedMockInteractions() {
        Mockito.verifyNoMoreInteractions(mockEncoderUtils);
    }

    @Test
    public void instantiate_AllGood_InstantiatesAsExpected() throws NoSuchAlgorithmException {
        final var request = new ApplicationCreateRequest();
        final var applicationType = new ApplicationTypeEntity();
        final var orgId = "the org id";
        final var name = "the app name";
        request.setName(name);

        when(mockEncoderUtils.cryptoSecureToHex(same(ApplicationEntityInstantiator.CLIENT_ID_SEED_LENGTH))).thenCallRealMethod();

        final var response = subject.instantiate(request, applicationType, orgId);

        verify(mockEncoderUtils).cryptoSecureToHex(same(ApplicationEntityInstantiator.CLIENT_ID_SEED_LENGTH));

        MatcherAssert.assertThat(response.getApplicationType(), sameInstance(applicationType));
        MatcherAssert.assertThat(response.getTenantId(), is(equalTo(orgId)));
        MatcherAssert.assertThat(response.getName(), is(equalTo(name)));
        MatcherAssert.assertThat(response.getClientId().length(), is(equalTo(ApplicationEntityInstantiator.CLIENT_ID_SEED_LENGTH * 2)));
    }

    @Test
    public void instantiate_ErrorEncoding_ExceptionThrown() throws NoSuchAlgorithmException {
        final var request = new ApplicationCreateRequest();
        final var appTypeId = "the app type id";
        final var applicationType = new ApplicationTypeEntity();
        applicationType.setId(appTypeId);
        final var orgId = "the org id";
        final var name = "the app name";
        request.setName(name);

        when(mockEncoderUtils.cryptoSecureToHex(same(ApplicationEntityInstantiator.CLIENT_ID_SEED_LENGTH))).thenThrow(new NoSuchAlgorithmException());

        final var exception = assertThrows(RuntimeException.class, () ->subject.instantiate(request, applicationType, orgId));

        verify(mockEncoderUtils).cryptoSecureToHex(same(ApplicationEntityInstantiator.CLIENT_ID_SEED_LENGTH));
        assertThat(exception.getMessage(), is(equalTo("error creating client id for application: " + appTypeId)));
    }
}
