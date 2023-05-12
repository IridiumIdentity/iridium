package software.iridium.api.instantiator;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import software.iridium.api.util.TokenGenerator;

import java.util.Date;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.same;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccessTokenEntityInstantiatorTest {

    @Mock
    private TokenGenerator mockTokenGenerator;
    @InjectMocks
    private AccessTokenEntityInstantiator subject;

    @AfterEach
    public void ensureNoUnexpectedMockInteractions() {
        Mockito.verifyNoMoreInteractions(mockTokenGenerator);
    }

    @Test
    public void instantiate_AllGood_BehavesAsExpected() {
        final var identityId = "the id";
        final var accessTokenValue = "the access token value";

        when(mockTokenGenerator.generateAccessToken(same(identityId), any(Date.class))).thenReturn(accessTokenValue);

        final var response = subject.instantiate(identityId);

        verify(mockTokenGenerator).generateAccessToken(same(identityId), any(Date.class));

        assertThat(response.getAccessToken(), is(equalTo(accessTokenValue)));
        assertThat(response.getTokenType(), is(equalTo("Bearer")));
        assertThat(response.getExpiration(), is(notNullValue()));
    }
}
