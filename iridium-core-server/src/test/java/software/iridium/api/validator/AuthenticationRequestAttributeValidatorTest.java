/*
 *  Copyright (C) Josh Fischer - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Josh Fischer <josh@joshfischer.io>, 2022.
 */

package software.iridium.api.validator;

import software.iridium.api.authentication.domain.AuthenticationRequest;
import software.iridium.api.util.AttributeValidator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthenticationRequestAttributeValidatorTest {

    @Mock
    private AttributeValidator mockAttributeValidator;
    @InjectMocks
    private AuthenticationRequestValidator subject;

    @AfterEach
    public void ensureNoUnexpectedMockInteractions() {
        Mockito.verifyNoMoreInteractions(mockAttributeValidator);
    }

    @Test
    public void validate_AllGood_ValidatesAsExpected() {
        final var username = "the username";
        final var password = "the password";
        final var clientId = "the application client id";
        final var request = new AuthenticationRequest();
        request.setUsername(username);
        request.setPassword(password);
        request.setClientId(clientId);

        when(mockAttributeValidator.isNotBlankAndNoLongerThan(anyString(), anyInt())).thenCallRealMethod();

        subject.validate(request);

        verify(mockAttributeValidator).isNotBlankAndNoLongerThan(same(username), eq(100));
        verify(mockAttributeValidator).isNotBlankAndNoLongerThan(same(password), eq(100));
        verify(mockAttributeValidator).isNotBlankAndNoLongerThan(same(clientId), eq(32));

    }

    @Test
    public void validate_ApplicationClientIdIsBlank_ValidatesAsExpected() {
        final var username = "the username";
        final var password = "dsfdf";
        final var clientId = "";
        final var request = new AuthenticationRequest();
        request.setUsername(username);
        request.setPassword(password);
        request.setClientId(clientId);

        when(mockAttributeValidator.isNotBlankAndNoLongerThan(anyString(), anyInt())).thenCallRealMethod();

        final var exception = assertThrows(IllegalArgumentException.class, () -> subject.validate(request));

        verify(mockAttributeValidator).isNotBlankAndNoLongerThan(same(username), eq(100));
        verify(mockAttributeValidator).isNotBlankAndNoLongerThan(same(password), eq(100));
        verify(mockAttributeValidator).isNotBlankAndNoLongerThan(same(clientId), eq(32));
        assertThat(exception.getMessage(), is(equalTo("clientId must not be blank and no longer than 32 characters")));
    }

    @Test
    public void validate_ApplicationClientIdIsTooLong_ValidatesAsExpected() {
        final var username = "the username";
        final var password = "the password";
        final var clientId = "dpfdspfd;fjds;fjdskjfldslfdskfdsljfdslfjdsflk" +
                "sdfldsjfldslfdslfdsfdslfdslfdslfdslfdsfldfldsfdslfdsfdsfjdsf" +
                "sfldsjfldsfldsfldsjfldsfjdslfdslfldsfldsfldslfjdslfldskfldskfd" +
                "s;fjdslfdslfdslfdslfdslfdksllselfjdslfkdslfkdslfkdslfjdslfjdslfdsjf" +
                "sdlfkjdslfdslfdsfjdslfjdslfjdslflkdsjfldsfdslfdslfdslfdlsfjldsfldsfdskjf" +
                "slfkdslfdslfldsfldskfldsfldsfldjslfjsdlfjldsfldsfldsjlfkjdlsfjlsdjfldsfjsdl";
        final var request = new AuthenticationRequest();
        request.setUsername(username);
        request.setPassword(password);
        request.setClientId(clientId);

        when(mockAttributeValidator.isNotBlankAndNoLongerThan(anyString(), anyInt())).thenCallRealMethod();

        final var exception = assertThrows(IllegalArgumentException.class, () -> subject.validate(request));

        verify(mockAttributeValidator).isNotBlankAndNoLongerThan(same(username), eq(100));
        verify(mockAttributeValidator).isNotBlankAndNoLongerThan(same(password), eq(100));
        verify(mockAttributeValidator).isNotBlankAndNoLongerThan(same(clientId), eq(32));
        assertThat(exception.getMessage(), is(equalTo("clientId must not be blank and no longer than 32 characters")));
    }

    @Test
    public void validate_PasswordIsBlank_ValidatesAsExpected() {
        final var username = "the username";
        final var password = "";
        final var request = new AuthenticationRequest();
        request.setUsername(username);
        request.setPassword(password);

        when(mockAttributeValidator.isNotBlankAndNoLongerThan(anyString(), anyInt())).thenCallRealMethod();

        final var exception = assertThrows(IllegalArgumentException.class, () -> subject.validate(request));

        verify(mockAttributeValidator).isNotBlankAndNoLongerThan(same(username), eq(100));
        verify(mockAttributeValidator).isNotBlankAndNoLongerThan(same(password), eq(100));
        assertThat(exception.getMessage(), is(equalTo("password must not blank and no longer than 100 characters")));
    }

    @Test
    public void validate_PasswordTooLong_ValidatesAsExpected() {
        final var username = "the username";
        final var password = "dpfdspfd;fjds;fjdskjfldslfdskfdsljfdslfjdsflk" +
                "sdfldsjfldslfdslfdsfdslfdslfdslfdslfdsfldfldsfdslfdsfdsfjdsf" +
                "sfldsjfldsfldsfldsjfldsfjdslfdslfldsfldsfldslfjdslfldskfldskfd" +
                "s;fjdslfdslfdslfdslfdslfdksllselfjdslfkdslfkdslfkdslfjdslfjdslfdsjf" +
                "sdlfkjdslfdslfdsfjdslfjdslfjdslflkdsjfldsfdslfdslfdslfdlsfjldsfldsfdskjf" +
                "slfkdslfdslfldsfldskfldsfldsfldjslfjsdlfjldsfldsfldsjlfkjdlsfjlsdjfldsfjsdl";
        final var request = new AuthenticationRequest();
        request.setUsername(username);
        request.setPassword(password);

        when(mockAttributeValidator.isNotBlankAndNoLongerThan(anyString(), anyInt())).thenCallRealMethod();

        final var exception = assertThrows(IllegalArgumentException.class, () -> subject.validate(request));

        verify(mockAttributeValidator).isNotBlankAndNoLongerThan(same(username), eq(100));
        verify(mockAttributeValidator).isNotBlankAndNoLongerThan(same(password), eq(100));
        assertThat(exception.getMessage(), is(equalTo("password must not blank and no longer than 100 characters")));
    }

    @Test
    public void validate_UsernameIsBlank_ValidatesAsExpected() {
        final var username = "";

        final var request = new AuthenticationRequest();
        request.setUsername(username);

        when(mockAttributeValidator.isNotBlankAndNoLongerThan(anyString(), anyInt())).thenCallRealMethod();

        final var exception = assertThrows(IllegalArgumentException.class, () -> subject.validate(request));

        verify(mockAttributeValidator).isNotBlankAndNoLongerThan(same(username), eq(100));
        assertThat(exception.getMessage(), is(equalTo("username must not blank and no longer than 100 characters: ")));
    }

    @Test
    public void validate_UsernameIsTooLong_ValidatesAsExpected() {
        final var username = "dpfdspfd;fjds;fjdskjfldslfdskfdsljfdslfjdsflk" +
                "sdfldsjfldslfdslfdsfdslfdslfdslfdslfdsfldfldsfdslfdsfdsfjdsf" +
                "sfldsjfldsfldsfldsjfldsfjdslfdslfldsfldsfldslfjdslfldskfldskfd" +
                "s;fjdslfdslfdslfdslfdslfdksllselfjdslfkdslfkdslfkdslfjdslfjdslfdsjf" +
                "sdlfkjdslfdslfdsfjdslfjdslfjdslflkdsjfldsfdslfdslfdslfdlsfjldsfldsfdskjf" +
                "slfkdslfdslfldsfldskfldsfldsfldjslfjsdlfjldsfldsfldsjlfkjdlsfjlsdjfldsfjsdl";

        final var request = new AuthenticationRequest();
        request.setUsername(username);

        when(mockAttributeValidator.isNotBlankAndNoLongerThan(anyString(), anyInt())).thenCallRealMethod();

        final var exception = assertThrows(IllegalArgumentException.class, () -> subject.validate(request));

        verify(mockAttributeValidator).isNotBlankAndNoLongerThan(same(username), eq(100));
        assertThat(exception.getMessage(), is(equalTo("username must not blank and no longer than 100 characters: " + username)));
    }
}
