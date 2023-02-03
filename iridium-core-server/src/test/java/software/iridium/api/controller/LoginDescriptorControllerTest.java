/*
 *  Copyright (C) Josh Fischer - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Josh Fischer <josh@joshfischer.io>, 2023.
 */
package software.iridium.api.controller;

import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import software.iridium.api.service.LoginDescriptorService;

@ExtendWith(MockitoExtension.class)
class LoginDescriptorControllerTest {

  @Mock private LoginDescriptorService mockDescriptorService;
  @InjectMocks private LoginDescriptorController subject;

  @Test
  public void get_AllGood_BehavesAsExpected() {
    final var clientId = "the client id";

    subject.getBySubdomain(clientId);

    verify(mockDescriptorService).getBySubdomain(same(clientId));
  }
}
