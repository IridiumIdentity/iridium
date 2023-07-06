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

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import software.iridium.api.authentication.ExternalIdentityProviderResponse;
import software.iridium.api.authentication.domain.*;
import software.iridium.api.base.error.NotAuthorizedException;
import software.iridium.api.base.error.ResourceNotFoundException;
import software.iridium.api.instantiator.ExternalIdentityProviderInstantiator;
import software.iridium.api.mapper.CreateExternalIdentityProviderResponseMapper;
import software.iridium.api.mapper.ExternalIdentityProviderResponseMapper;
import software.iridium.api.mapper.ExternalIdentityProviderSummaryResponseMapper;
import software.iridium.api.mapper.ExternalIdentityProviderUpdateResponseMapper;
import software.iridium.api.repository.ExternalIdentityProviderEntityRepository;
import software.iridium.api.repository.ExternalIdentityProviderTemplateEntityRepository;
import software.iridium.api.repository.TenantEntityRepository;
import software.iridium.api.updator.ExternalIdentityProviderUpdator;
import software.iridium.api.util.AttributeValidator;
import software.iridium.api.validator.CreateExternalIdentityProviderRequestValidator;
import software.iridium.api.validator.ExternalIdentityProviderUpdateRequestValidator;

@Service
public class ExternalIdentityProviderService {

  @Autowired private AttributeValidator validator;
  @Autowired private CreateExternalIdentityProviderRequestValidator requestValidator;
  @Autowired private ExternalIdentityProviderTemplateEntityRepository templateRepository;
  @Autowired private TenantEntityRepository tenantRepository;
  @Autowired private ExternalIdentityProviderEntityRepository providerRepository;
  @Autowired private ExternalIdentityProviderInstantiator providerInstantiator;
  @Autowired private CreateExternalIdentityProviderResponseMapper createResponseMapper;
  @Autowired private ExternalIdentityProviderSummaryResponseMapper summaryMapper;
  @Autowired private ExternalIdentityProviderUpdateRequestValidator updateRequestValidator;
  @Autowired private ExternalIdentityProviderUpdator externalIdentityProviderUpdator;
  @Autowired private ExternalIdentityProviderUpdateResponseMapper updateResponseMapper;
  @Autowired private ExternalIdentityProviderResponseMapper responseMapper;

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

    return createResponseMapper.map(
        providerRepository.save(providerInstantiator.instantiate(tenant, request, template)));
  }

  @Transactional(propagation = Propagation.SUPPORTS)
  public List<ExternalIdentityProviderSummaryResponse> retrieveAllSummaries(final String tenantId) {
    checkArgument(validator.isUuid(tenantId), "tenantId must be a valid uuid");

    return summaryMapper.mapList(providerRepository.findAll());
  }

  @Transactional(propagation = Propagation.REQUIRED)
  public ExternalIdentityProviderUpdateResponse update(
      final ExternalIdentityProviderUpdateRequest request,
      final String tenantId,
      final String externalProviderId) {
    checkArgument(validator.isUuid(tenantId), "tenantId must be a valid uuid");
    checkArgument(validator.isUuid(externalProviderId), "externalProviderId must be a valid uuid");
    updateRequestValidator.validate(request);

    final var externalProvider =
        providerRepository
            .findById(externalProviderId)
            .orElseThrow(() -> new ResourceNotFoundException("external provider not found"));

    if (validator.doesNotEqual(externalProvider.getTenant().getId(), tenantId)) {
      throw new NotAuthorizedException("invalid data request");
    }
    return updateResponseMapper.map(
        providerRepository.save(externalIdentityProviderUpdator.update(externalProvider, request)));
  }

  @Transactional(propagation = Propagation.SUPPORTS)
  public ExternalIdentityProviderResponse get(
      final String tenantId, final String externalProviderId) {
    checkArgument(validator.isUuid(tenantId), "tenantId must be a valid uuid");
    checkArgument(validator.isUuid(externalProviderId), "externalProviderId must be a valid uuid");

    final var entity =
        providerRepository
            .findById(externalProviderId)
            .orElseThrow(() -> new ResourceNotFoundException("external provider not found for id"));

    if (validator.doesNotEqual(tenantId, entity.getTenant().getId())) {
      throw new NotAuthorizedException();
    }
    return responseMapper.map(entity);
  }
}
