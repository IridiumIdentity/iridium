/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
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
import software.iridium.api.mapper.ProviderSummaryResponseMapper;
import software.iridium.api.repository.ExternalIdentityProviderTemplateEntityRepository;
import software.iridium.entity.ExternalIdentityProviderTemplateEntity;

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
