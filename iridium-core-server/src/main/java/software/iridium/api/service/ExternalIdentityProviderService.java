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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import software.iridium.api.authentication.domain.CreateExternalIdentityProviderRequest;
import software.iridium.api.authentication.domain.CreateExternalIdentityProviderResponse;
import software.iridium.api.base.error.ResourceNotFoundException;
import software.iridium.api.instantiator.ExternalIdentityProviderInstantiator;
import software.iridium.api.mapper.CreateExternalIdentityProviderResponseMapper;
import software.iridium.api.repository.ExternalIdentityProviderEntityRepository;
import software.iridium.api.repository.ExternalIdentityProviderTemplateEntityRepository;
import software.iridium.api.repository.TenantEntityRepository;
import software.iridium.api.util.AttributeValidator;
import software.iridium.api.validator.CreateExternalIdentityProviderRequestValidator;

@Service
public class ExternalIdentityProviderService {

  @Autowired private AttributeValidator validator;
  @Autowired private CreateExternalIdentityProviderRequestValidator requestValidator;
  @Autowired private ExternalIdentityProviderTemplateEntityRepository templateRepository;
  @Autowired private TenantEntityRepository tenantRepository;
  @Autowired private ExternalIdentityProviderEntityRepository providerRepository;
  @Autowired private ExternalIdentityProviderInstantiator providerInstantiator;
  @Autowired private CreateExternalIdentityProviderResponseMapper responseMapper;

  @Transactional(propagation = Propagation.REQUIRED)
  public CreateExternalIdentityProviderResponse create(
      final String tenantId, final CreateExternalIdentityProviderRequest request) {
    checkArgument(validator.isUuid(tenantId), "tenantId must be a valid uuid");
    requestValidator.validate(request);

    final var template =
        templateRepository
            .findById(request.getExternalProviderTemplateId())
            .orElseThrow(
                () ->
                    new ResourceNotFoundException(
                        "provider template not found for id: "
                            + request.getExternalProviderTemplateId()));

    final var tenant =
        tenantRepository
            .findById(tenantId)
            .orElseThrow(() -> new ResourceNotFoundException("tenant not found"));

    return responseMapper.map(
        providerRepository.save(providerInstantiator.instantiate(tenant, request, template)));
  }
}
