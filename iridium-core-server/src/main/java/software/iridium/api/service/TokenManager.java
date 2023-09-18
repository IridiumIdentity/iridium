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
import java.util.Date;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import software.iridium.api.base.error.NotAuthorizedException;
import software.iridium.api.repository.AccessTokenEntityRepository;
import software.iridium.api.repository.ApplicationEntityRepository;
import software.iridium.api.repository.AuthenticationEntityRepository;
import software.iridium.api.repository.IdentityEntityRepository;
import software.iridium.api.util.AttributeValidator;
import software.iridium.api.util.ServletTokenExtractor;
import software.iridium.entity.IdentityEntity;

@Component
public class TokenManager {

  @Autowired private AuthenticationEntityRepository authenticationEntityRepository;
  @Autowired private AuthenticationGenerator authenticationGenerator;
  @Autowired private ServletTokenExtractor tokenExtractor;
  @Autowired private AccessTokenEntityRepository accessTokenRepository;
  @Autowired private AttributeValidator validator;
  @Autowired private IdentityEntityRepository identityRepository;
  @Autowired private ApplicationEntityRepository applicationRepository;

  @Transactional(propagation = Propagation.REQUIRED)
  public ImmutablePair<String, String> getOrGenerateToken(IdentityEntity identityEntity) {
    final var authenticationOptional =
        authenticationEntityRepository.findFirstByIdentityIdOrderByCreatedDesc(
            identityEntity.getId());

    if (authenticationOptional.isEmpty()
        || new Date().after(authenticationOptional.get().getExpiration())) {
      final var generatedAuthentication =
          authenticationGenerator.generateAuthentication(identityEntity);
      authenticationEntityRepository.save(generatedAuthentication);
      return new ImmutablePair<>(
          generatedAuthentication.getAuthToken(), generatedAuthentication.getRefreshToken());
    }

    return new ImmutablePair<>(
        authenticationOptional.get().getAuthToken(),
        authenticationOptional.get().getRefreshToken());
  }

  @Transactional(propagation = Propagation.REQUIRED)
  public void logout(final String clientId, final HttpServletRequest request) {
    checkArgument(validator.isNotBlank(clientId), "clientId must not be blank");

    final var tokenValue = tokenExtractor.extractBearerToken(request);

    final var accessTokenOptional =
        accessTokenRepository.findFirstByAccessTokenAndExpirationAfter(tokenValue, new Date());

    if (accessTokenOptional.isEmpty()) {
      return;
    }

    final var accessToken = accessTokenOptional.get();

    final var identity =
        identityRepository
            .findById(accessToken.getIdentityId())
            .orElseThrow(() -> new NotAuthorizedException("Not Authorized"));

    final var application =
        applicationRepository
            .findByClientId(clientId)
            .orElseThrow(() -> new NotAuthorizedException("Not Authorized"));

    if (application.getTenantId().equals(identity.getParentTenantId())) {
      accessTokenRepository.delete(accessTokenOptional.get());
    }
  }
}
