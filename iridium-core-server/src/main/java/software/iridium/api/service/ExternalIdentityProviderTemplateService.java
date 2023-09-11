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

import static com.google.common.base.Preconditions.checkArgument;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import software.iridium.api.authentication.domain.ExternalProviderTemplateSummaryResponse;
import software.iridium.api.base.error.ResourceNotFoundException;
import software.iridium.api.mapper.ProviderTemplateSummaryResponseMapper;
import software.iridium.api.repository.ExternalIdentityProviderTemplateEntityRepository;
import software.iridium.api.repository.TenantEntityRepository;
import software.iridium.api.util.AttributeValidator;
import software.iridium.entity.ExternalIdentityProviderEntity;
import software.iridium.entity.ExternalIdentityProviderTemplateEntity;

@Service
public class ExternalIdentityProviderTemplateService {

  @Autowired private ExternalIdentityProviderTemplateEntityRepository providerTemplateRepository;
  @Autowired private ProviderTemplateSummaryResponseMapper responseMapper;
  @Autowired private AttributeValidator validator;
  @Autowired private TenantEntityRepository tenantRepository;

  @Transactional(propagation = Propagation.SUPPORTS)
  public List<ExternalProviderTemplateSummaryResponse> retrieveAllSummaries() {

    return responseMapper.mapList(providerTemplateRepository.findAll());
  }

  // todo: (joshfischer) add integration test
  @Transactional(propagation = Propagation.SUPPORTS)
  public List<ExternalProviderTemplateSummaryResponse> retrieveAvailableSummaries(
      final String tenantId) {
    checkArgument(validator.isUuid(tenantId), "tenantId must be a valid uuid");

    final var templates = providerTemplateRepository.findAll();

    final var tenant =
        tenantRepository
            .findById(tenantId)
            .orElseThrow(() -> new ResourceNotFoundException("tenant not found"));

    final var templatesToReturn = new ArrayList<ExternalIdentityProviderTemplateEntity>();
    for (ExternalIdentityProviderTemplateEntity providerTemplate : templates) {
      var templateNotFound = true;
      for (ExternalIdentityProviderEntity provider : tenant.getExternalIdentityProviders()) {
        if (provider.getTemplate().equals(providerTemplate)) {
          // this template has already been used
          templateNotFound = false;
          break;
        }
      }
      if (templateNotFound) {
        templatesToReturn.add(providerTemplate);
      }
    }
    return responseMapper.mapList(templatesToReturn);
  }
}
