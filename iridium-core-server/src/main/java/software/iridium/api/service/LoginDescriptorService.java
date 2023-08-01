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
import software.iridium.api.authentication.domain.LoginDescriptorResponse;
import software.iridium.api.authentication.domain.TenantLogoUrlUpdateRequest;
import software.iridium.api.authentication.domain.TenantLogoUrlUpdateResponse;
import software.iridium.api.base.error.ResourceNotFoundException;
import software.iridium.api.mapper.LoginDescriptorResponseMapper;
import software.iridium.api.mapper.TenantLogoUpdateResponseMapper;
import software.iridium.api.repository.LoginDescriptorEntityRepository;
import software.iridium.api.updator.TenantLogoUrlUpdator;
import software.iridium.api.util.AttributeValidator;
import software.iridium.api.validator.TenantLogoUrlUpdateRequestValidator;

@Service
public class LoginDescriptorService {

  @Autowired private LoginDescriptorEntityRepository loginDescriptorRepository;
  @Autowired private LoginDescriptorResponseMapper responseMapper;
  @Autowired private AttributeValidator attributeValidator;
  @Autowired private TenantLogoUrlUpdateRequestValidator logoUpdateRequestValidator;
  @Autowired private TenantLogoUrlUpdator logoUrlUpdator;
  @Autowired private TenantLogoUpdateResponseMapper logoUpdateResponseMapper;

  @Transactional(propagation = Propagation.SUPPORTS)
  public LoginDescriptorResponse getBySubdomain(final String subdomain) {
    checkArgument(attributeValidator.isValidSubdomain(subdomain), "subdomain must be valid format");

    final var loginDescriptor =
        loginDescriptorRepository
            .findByTenant_Subdomain(subdomain)
            .orElseThrow(
                () ->
                    new ResourceNotFoundException(
                        "login descriptor not found for subdomain: " + subdomain));

    return responseMapper.map(loginDescriptor);
  }

  @Transactional(propagation = Propagation.REQUIRED)
  public TenantLogoUrlUpdateResponse updateLogoURL(
      final TenantLogoUrlUpdateRequest request, final String tenantId) {
    checkArgument(attributeValidator.isUuid(tenantId), "tenantId must be valid uuid");
    logoUpdateRequestValidator.validate(request);

    final var loginDescriptor =
        loginDescriptorRepository
            .findByTenantId(tenantId)
            .orElseThrow(() -> new ResourceNotFoundException("descriptor not found for id"));

    return logoUpdateResponseMapper.map(
        loginDescriptorRepository.save(logoUrlUpdator.update(loginDescriptor, request)));
  }

  @Transactional(propagation = Propagation.SUPPORTS)
  public LoginDescriptorResponse getByTenantId(final String tenantId) {
    checkArgument(attributeValidator.isUuid(tenantId), "tenantId must be valid uuid");

    final var loginDescriptor =
            loginDescriptorRepository
                    .findByTenantId(tenantId)
                    .orElseThrow(
                            () ->
                                    new ResourceNotFoundException(
                                            "login descriptor not found for tenant id"));

    return responseMapper.map(loginDescriptor);
  }
}
