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

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import software.iridium.api.authentication.domain.CreateTenantRequest;
import software.iridium.api.authentication.domain.CreateTenantResponse;
import software.iridium.api.authentication.domain.TenantSummary;
import software.iridium.api.base.error.DuplicateResourceException;
import software.iridium.api.base.error.ResourceNotFoundException;
import software.iridium.api.instantiator.LoginDescriptorEntityInstantiator;
import software.iridium.api.instantiator.TenantInstantiator;
import software.iridium.api.mapper.CreateTenantResponseMapper;
import software.iridium.api.mapper.TenantSummaryMapper;
import software.iridium.api.repository.IdentityEntityRepository;
import software.iridium.api.repository.TenantEntityRepository;
import software.iridium.api.util.AttributeValidator;

@Service
public class TenantService {

  @Autowired private IdentityService identityService;
  @Autowired private TenantEntityRepository tenantRepository;
  @Autowired private TenantSummaryMapper summaryMapper;
  @Autowired private AttributeValidator attributeValidator;
  @Autowired private TenantInstantiator tenantInstantiator;
  @Autowired private IdentityEntityRepository identityRepository;
  @Autowired private CreateTenantResponseMapper responseMapper;
  @Autowired private LoginDescriptorEntityInstantiator loginDescriptorInstantiator;

  @Transactional(propagation = Propagation.SUPPORTS)
  public List<TenantSummary> getTenantSummaries(final HttpServletRequest request) {

    final var identity = identityService.getIdentity(request);
    final var tenants =
        tenantRepository
            .findByIdIn(identity.getTenantIds())
            .orElseThrow(
                () ->
                    new ResourceNotFoundException(
                        "one or more tenants not found for id(s) " + identity.getTenantIds()));
    return summaryMapper.mapToList(tenants);
  }

  @Transactional(propagation = Propagation.REQUIRED)
  public CreateTenantResponse create(
      final HttpServletRequest servletRequest, final CreateTenantRequest request) {
    checkArgument(
        attributeValidator.isValidSubdomain(request.getSubdomain()),
        "subdomain must only contains alphanumeric characters and hyphens");
    checkArgument(
        attributeValidator.isNotBlankAndNoLongerThan(request.getSubdomain(), 100),
        "subdomain must not be blank and no longer than 100 characters");
    checkArgument(
        attributeValidator.isNotNull(request.getEnvironment()), "environment must not be null");

    if (tenantRepository.findBySubdomain(request.getSubdomain()).isPresent()) {
      throw new DuplicateResourceException("duplicate subdomain: " + request.getSubdomain());
    }

    final var identityResponse = identityService.getIdentity(servletRequest);

    final var identity =
        identityRepository
            .findById(identityResponse.getId())
            .orElseThrow(
                () ->
                    new ResourceNotFoundException(
                        "identity not found for id: " + identityResponse.getId()));

    final var tenant = tenantRepository.save(tenantInstantiator.instantiate(request));
    // todo: fix this ugliness
    loginDescriptorInstantiator.instantiate(tenant);

    tenant.getManagingIdentities().add(identity);
    identity.getManagedTenants().add(tenant);

    return responseMapper.map(tenant);
  }
}
