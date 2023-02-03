/*
 *  Copyright (C) Josh Fischer - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Josh Fischer <josh@joshfischer.io>, 2023.
 */
package software.iridium.api.controller;

import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import software.iridium.api.service.ApplicationTypeService;

@ExtendWith(MockitoExtension.class)
class ApplicationTypeControllerTest {

  @Mock private ApplicationTypeService mockApplicationTypeService;
  @InjectMocks private ApplicationTypeController subject;

  @AfterEach
  public void ensureNoUnexpectedMockInteractions() {
    Mockito.verifyNoMoreInteractions(mockApplicationTypeService);
  }

  @Test
  public void getAll_BehavesAsExpected() {

    subject.getAll();

    verify(mockApplicationTypeService).getAll();
  }
}
