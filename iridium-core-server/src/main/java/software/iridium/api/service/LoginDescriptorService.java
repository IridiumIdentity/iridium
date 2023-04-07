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

import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import software.iridium.api.authentication.domain.LoginDescriptorCreateRequest;
import software.iridium.api.authentication.domain.LoginDescriptorResponse;
import software.iridium.api.base.error.ResourceNotFoundException;
import software.iridium.api.mapper.LoginDescriptorResponseMapper;
import software.iridium.api.repository.LoginDescriptorEntityRepository;
import software.iridium.api.util.AttributeValidator;

@Service
public class LoginDescriptorService {

  @Resource private LoginDescriptorEntityRepository loginDescriptorRepository;
  @Resource private LoginDescriptorResponseMapper responseMapper;
  @Resource private AttributeValidator attributeValidator;

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

  public LoginDescriptorResponse create(
      final LoginDescriptorCreateRequest request, final String tenantId) {
    return null;
  }
}
