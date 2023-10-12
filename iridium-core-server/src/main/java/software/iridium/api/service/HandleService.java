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
import software.iridium.api.base.error.NotAuthorizedException;
import software.iridium.api.base.error.ResourceNotFoundException;
import software.iridium.api.repository.EmailVerificationTokenEntityRepository;
import software.iridium.api.repository.TenantEntityRepository;
import software.iridium.api.util.AttributeValidator;

@Service
public class HandleService {
  @Autowired private AttributeValidator validator;
  @Autowired private TenantEntityRepository tenantRepository;
  @Autowired private EmailVerificationTokenEntityRepository verificationTokenRepository;

  @Transactional(propagation = Propagation.REQUIRED)
  public void verify(final String tenantId, final String token) {
    checkArgument(validator.isUuid(tenantId), "tenantId must be a valid uuid");
    checkArgument(validator.isNotBlank(token), "token is required");

    if (tenantRepository.findById(tenantId).isEmpty()) {
      throw new ResourceNotFoundException("Resource Not Found");
    }

    final var verificationToken =
        verificationTokenRepository
            .findByToken(token)
            .orElseThrow(() -> new ResourceNotFoundException("Resource Not Found"));
    final var email = verificationToken.getEmail();

    if (validator.doesNotEqual(email.getIdentity().getParentTenantId(), tenantId)) {
      throw new NotAuthorizedException("Not Authorized");
    }
    email.setVerified(true);
    email.setEmailVerificationToken(null);
  }
}
