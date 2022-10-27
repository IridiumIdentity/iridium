/*
 *  Copyright (C) Josh Fischer - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Josh Fischer <josh@joshfischer.io>, 2022.
 */

package software.iridium.api.validator;

import software.iridium.api.util.AuthorizationCodeFlowConstants;
import software.iridium.api.util.AttributeValidator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthorizationGrantTypeParamAttributeValidatorTest {

    @Mock
    private AttributeValidator mockAttributeValidator;
    @InjectMocks
    private AuthorizationGrantTypeParamValidator subject;

    @AfterEach
    public void ensureNoUnexpectedMockInteractions() {
        Mockito.verifyNoMoreInteractions(
                mockAttributeValidator
        );
    }

    @Test
    public void validate_AllGood_BehavesAsExpected() {
        final var clientId = "the client id";
        final var params = new HashMap<String, String>();
        params.put(AuthorizationCodeFlowConstants.RESPONSE_TYPE.getValue(), AuthorizationCodeFlowConstants.AUTHORIZATION_CODE.getValue());
        params.put(AuthorizationCodeFlowConstants.CLIENT_ID.getValue(), clientId);

        when(mockAttributeValidator.isNotBlank(anyString())).thenCallRealMethod();

        subject.validate(params);

        verify(mockAttributeValidator).isNotBlank(eq(AuthorizationCodeFlowConstants.AUTHORIZATION_CODE.getValue()));
        verify(mockAttributeValidator).isNotBlank(same(clientId));
    }

    @Test
    public void validate_responseTypeIsBlank_ExceptionThrown() {
        final var params = new HashMap<String, String>();

        when(mockAttributeValidator.isNotBlank(anyString())).thenCallRealMethod();

        final var exception = assertThrows(IllegalArgumentException.class, ()-> subject.validate(params));

        assertThat(exception.getMessage(), is(equalTo("response_type must not be blank")));

        verify(mockAttributeValidator).isNotBlank(eq(""));
    }

    @Test
    public void validate_ClientIdIsBlank_ExceptionThrown() {
        final var params = new HashMap<String, String>();
        params.put(AuthorizationCodeFlowConstants.RESPONSE_TYPE.getValue(), AuthorizationCodeFlowConstants.AUTHORIZATION_CODE.getValue());

        when(mockAttributeValidator.isNotBlank(anyString())).thenCallRealMethod();

        final var exception = assertThrows(IllegalArgumentException.class, ()-> subject.validate(params));

        assertThat(exception.getMessage(), is(equalTo("client_id must not be blank")));

        verify(mockAttributeValidator).isNotBlank(eq(AuthorizationCodeFlowConstants.AUTHORIZATION_CODE.getValue()));
        verify(mockAttributeValidator).isNotBlank(eq(""));
    }
}
