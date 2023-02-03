/*
 *  Copyright (C) Josh Fischer - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Josh Fischer <josh@joshfischer.io>, 2023.
 */
package software.iridium.api.controller;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.sameInstance;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.*;

import javax.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import software.iridium.api.authentication.domain.IdentityResponse;
import software.iridium.api.service.IdentityService;

@ExtendWith(MockitoExtension.class)
class IdentityControllerTest {

  @Mock private IdentityService mockIdentityService;
  @Mock private HttpServletRequest mockServletRequest;
  @InjectMocks private IdentityController subject;

  @AfterEach
  public void verifyNoUnexpectedMockInteractions() {
    Mockito.verifyNoMoreInteractions(mockIdentityService);
  }

  @Test
  public void getIdentity_AllGood_BehavesAsExpected() {
    final var response = new IdentityResponse();

    when(mockIdentityService.getIdentity(same(mockServletRequest))).thenReturn(response);

    assertThat(subject.getIdentity(mockServletRequest).getData(), sameInstance(response));

    verify(mockIdentityService).getIdentity(same(mockServletRequest));
  }
}
