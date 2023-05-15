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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import software.iridium.api.authentication.domain.ApplicationCreateRequest;
import software.iridium.api.authentication.domain.ApplicationCreateResponse;
import software.iridium.api.authentication.domain.ApplicationSummary;
import software.iridium.api.base.domain.PagedListResponse;
import software.iridium.api.base.error.DuplicateResourceException;
import software.iridium.api.base.error.ResourceNotFoundException;
import software.iridium.api.entity.ApplicationEntity;
import software.iridium.api.entity.ApplicationType;
import software.iridium.api.instantiator.ApplicationEntityInstantiator;
import software.iridium.api.mapper.ApplicationResponseMapper;
import software.iridium.api.mapper.ApplicationSummaryMapper;
import software.iridium.api.repository.ApplicationEntityRepository;
import software.iridium.api.repository.ApplicationTypeEntityRepository;
import software.iridium.api.repository.TenantEntityRepository;
import software.iridium.api.util.AttributeValidator;
import software.iridium.api.validator.SinglePageApplicationCreateRequestValidator;

@Service
public class ApplicationService {

  @Autowired private AttributeValidator attributeValidator;
  @Autowired private TenantEntityRepository tenantRepository;
  @Autowired private ApplicationEntityRepository applicationRepository;
  @Autowired private ApplicationTypeEntityRepository applicationTypeRepository;
  @Autowired private ApplicationEntityInstantiator entityInstantiator;
  @Autowired private ApplicationResponseMapper responseMapper;
  @Autowired private ApplicationSummaryMapper summaryMapper;
  @Autowired private SinglePageApplicationCreateRequestValidator spaRequestValidator;

  @Transactional(propagation = Propagation.REQUIRED)
  public ApplicationCreateResponse create(
      final ApplicationCreateRequest request, final String tenantId) {
    checkArgument(
        attributeValidator.isUuid(tenantId),
        "tenant id must be a properly formatted uuid: " + tenantId);
    checkArgument(
        attributeValidator.isNotBlankAndNoLongerThan(request.getName(), 100),
        "tenant name must not be blank and less than 100 characters");
    checkArgument(
        attributeValidator.isUuid(request.getApplicationTypeId()),
        "applicationTypeId must be a properly formatted uuid: " + request.getApplicationTypeId());

    final var applicationType =
        applicationTypeRepository
            .findById(request.getApplicationTypeId())
            .orElseThrow(
                () ->
                    new ResourceNotFoundException(
                        "application type not found for id: " + request.getApplicationTypeId()));

    if (applicationType.getType().equals(ApplicationType.SINGLE_PAGE)) {
      spaRequestValidator.validate(request);
    }

    if (tenantRepository.findById(tenantId).isEmpty()) {
      throw new ResourceNotFoundException("tenant not found for id: " + tenantId);
    }

    if (applicationRepository.findByNameAndTenantId(request.getName(), tenantId).isPresent()) {
      throw new DuplicateResourceException(
          String.format(
              "duplicate application name: %s for tenant %s", request.getName(), tenantId));
    }
    return responseMapper.map(
        applicationRepository.save(
            entityInstantiator.instantiate(request, applicationType, tenantId)));
  }

  @Transactional(propagation = Propagation.SUPPORTS)
  public PagedListResponse<ApplicationSummary> getPageByTenantId(
      final String tenantId,
      final Integer page,
      final Integer size,
      final Boolean active) {
    checkArgument(
        attributeValidator.isUuid(tenantId), "tenantId must be a valid uuid: " + tenantId);
    checkArgument(attributeValidator.isZeroOrGreater(page), "page must be a zero or greater integer: " + page);
    checkArgument(attributeValidator.isPositive(size), "size must be a positive integer: " + size);
    checkArgument(
        attributeValidator.isNotNull(active), "active must be either true or false: " + active);

    Page<ApplicationEntity> pageOfEntityInstances =
        applicationRepository.findAllByTenantIdAndActive(
            tenantId, active, PageRequest.of(page, size));
    final var content = pageOfEntityInstances.getContent();
    return new PagedListResponse<>(
        summaryMapper.mapToSummaries(content), pageOfEntityInstances.getTotalPages(), page, size);
  }
}
