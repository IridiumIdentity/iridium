/*
 *  Copyright (C) Josh Fischer - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Josh Fischer <josh@joshfischer.io>, 2023.
 */
package software.iridium.email.api.controller;

import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import software.iridium.email.api.service.EmailTemplateService;

@ExtendWith(MockitoExtension.class)
class EmailTemplatesControllerTest {

  @Mock private EmailTemplateService mockTemplateService;
  @InjectMocks private EmailTemplateController subject;

  @AfterEach
  public void ensureNoUnexpectedMockInteractions() {
    Mockito.verifyNoMoreInteractions(mockTemplateService);
  }

  @Test
  public void get_AllGood_BehavesAsExpected() {
    final String id = "the id";

    subject.get(id);

    verify(mockTemplateService).get(same(id));
  }
}
