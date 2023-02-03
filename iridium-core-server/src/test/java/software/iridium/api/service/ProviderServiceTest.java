/*
 *  Copyright (C) Josh Fischer - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Josh Fischer <josh@joshfischer.io>, 2023.
 */
package software.iridium.api.service;

import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import software.iridium.api.entity.ExternalIdentityProviderTemplateEntity;
import software.iridium.api.mapper.ProviderSummaryResponseMapper;
import software.iridium.api.repository.ExternalIdentityProviderTemplateEntityRepository;

@ExtendWith(MockitoExtension.class)
class ProviderServiceTest {

  @Mock private ExternalIdentityProviderTemplateEntityRepository mockProviderRepository;
  @Mock private ProviderSummaryResponseMapper mockResponseMapper;
  @InjectMocks private ProviderService subject;

  @AfterEach
  public void ensureNoUnexpectedMockInteractions() {
    Mockito.verifyNoMoreInteractions(mockProviderRepository, mockResponseMapper);
  }

  @Test
  public void retrieveAllSummaries_AllGood_BehavesAsExpected() {
    final List<ExternalIdentityProviderTemplateEntity> entities = new ArrayList<>();

    when(mockProviderRepository.findAll()).thenReturn(entities);

    subject.retrieveAllSummaries();

    verify(mockProviderRepository).findAll();
    verify(mockResponseMapper).mapList(same(entities));
  }
}
