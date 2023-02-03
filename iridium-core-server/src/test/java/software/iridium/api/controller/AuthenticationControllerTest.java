/*
 *  Copyright (C) Josh Fischer - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Josh Fischer <josh@joshfischer.io>, 2023.
 */
package software.iridium.api.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import software.iridium.api.service.AuthenticationService;

@ExtendWith(MockitoExtension.class)
class AuthenticationControllerTest {

  @Mock private AuthenticationService mockAuthenticationService;
  @InjectMocks private AuthenticationController subject;

  @AfterEach
  public void verifyNoUnexpectedMockInteractions() {
    Mockito.verifyNoMoreInteractions(mockAuthenticationService);
  }
  // todo
  //    @Test
  //    public void authenticateUser_AllGood_BehavesAsExpected() {
  //        final var request = new AuthenticationRequest();
  //
  //        subject.authenticateUser(request);
  //
  //        //todo verify(mockAuthenticationService).authenticate(same(request));
  //    }
}
