/*
 *  Copyright (C) Josh Fischer - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Josh Fischer <josh@joshfischer.io>, 2023.
 */
package software.iridium.api.instantiator;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.sameInstance;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;
import software.iridium.api.entity.IdentityEntity;
import software.iridium.api.util.DateUtils;

@ExtendWith(MockitoExtension.class)
class PasswordResetTokenEntityInstantiatorTest {

  @Mock private BCryptPasswordEncoder mockEncoder;
  @InjectMocks private PasswordResetTokenEntityInstantiator subject;

  @AfterEach
  public void ensureNoUnexpectedMockInteractions() {
    Mockito.verifyNoMoreInteractions(mockEncoder);
  }

  @Test
  public void instantiate_AllGood_InstantiatesAsExpected() {
    ReflectionTestUtils.setField(subject, "passwordResetTokenLifetime", 4);
    final var resetToken = "the reset token";
    final var identity = new IdentityEntity();

    when(mockEncoder.encode(anyString())).thenReturn(resetToken);

    final var response = subject.instantiate(identity);

    verify(mockEncoder).encode(anyString());

    MatcherAssert.assertThat(response.getIdentity(), sameInstance(identity));
    MatcherAssert.assertThat(response.getToken(), is(equalTo(resetToken)));
    Assertions.assertTrue(response.getExpiration().before(DateUtils.addHoursToCurrentTime(5)));
    Assertions.assertTrue(response.getExpiration().after(DateUtils.addHoursToCurrentTime(3)));
  }
}
