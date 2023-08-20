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
package software.iridium.api.validator;

import static com.google.common.base.Preconditions.checkArgument;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import software.iridium.api.authentication.domain.CreatePasskeyRequest;
import software.iridium.api.authentication.domain.WebAuthNClientData;
import software.iridium.api.base.error.ResourceNotFoundException;
import software.iridium.api.repository.ChallengeEntityRepository;
import software.iridium.api.util.AttributeValidator;

@Component
public class CreatePasskeyRequestValidator {
  private static final Logger logger = LoggerFactory.getLogger(CreatePasskeyRequestValidator.class);
  @Autowired private AttributeValidator attributeValidator;
  @Autowired private ChallengeEntityRepository challengeRepository;

  @Transactional(propagation = Propagation.REQUIRED)
  public void validate(final CreatePasskeyRequest request) {
    logger.info("getClientDataJson: {}", new String(request.getClientDataJSON()));
    final var objectMapper =
        new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    WebAuthNClientData clientData;
    try {
      clientData =
          objectMapper.readValue(new String(request.getClientDataJSON()), WebAuthNClientData.class);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    checkArgument(
        attributeValidator.equals(clientData.getType(), "webauthn.create"),
        "clientData.type invalid");
    final var decodedChallenge =
        new String(
            Base64.getUrlDecoder()
                .decode(clientData.getChallenge().getBytes(StandardCharsets.UTF_8)));

    // check that decoded string looked up a challenge with the same origin
    final var challenge =
        challengeRepository
            .findByChallenge(decodedChallenge)
            .orElseThrow(() -> new ResourceNotFoundException(""));

    checkArgument(
        attributeValidator.equals(challenge.getChallenge(), decodedChallenge),
        "challenge does not match");
    checkArgument(
        attributeValidator.equals(challenge.getOrigin(), clientData.getOrigin()),
        "invalid challenge origin");
  }
}
