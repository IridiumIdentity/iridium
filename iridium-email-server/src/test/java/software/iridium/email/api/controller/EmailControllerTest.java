/*
 *  Copyright (C) Josh Fischer - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Josh Fischer <josh@joshfischer.io>, 2023.
 */
package software.iridium.email.api.controller;

import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import software.iridium.api.email.domain.EmailSendRequest;
import software.iridium.email.api.service.EmailService;

@ExtendWith(MockitoExtension.class)
class EmailControllerTest {

  @Mock private EmailService mockEmailService;
  @InjectMocks private EmailController subject;

  @Test
  public void send_AllGood_BehavesAsExpected() {
    final var request = new EmailSendRequest();

    subject.send(request);

    verify(mockEmailService).send(same(request));
  }
}
