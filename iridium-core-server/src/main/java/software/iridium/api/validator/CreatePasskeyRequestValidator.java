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

import com.google.common.hash.Hashing;
import com.google.iot.cbor.CborMap;
import com.google.iot.cbor.CborParseException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
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
import software.iridium.api.util.UnknownPropertyStringObjectMapper;

@Component
public class CreatePasskeyRequestValidator {
  private static final Logger logger = LoggerFactory.getLogger(CreatePasskeyRequestValidator.class);
  @Autowired private AttributeValidator attributeValidator;
  @Autowired private ChallengeEntityRepository challengeRepository;
  @Autowired private UnknownPropertyStringObjectMapper objectMapper;

  @Transactional(propagation = Propagation.REQUIRED)
  public void validate(final CreatePasskeyRequest request) {
    logger.info("getClientDataJson: {}", new String(request.getClientDataJSON()));

    final var clientData =
        objectMapper.map(new String(request.getClientDataJSON()), WebAuthNClientData.class);

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
            .orElseThrow(() -> new ResourceNotFoundException("invalid challenge"));

    checkArgument(
        attributeValidator.equals(challenge.getChallenge(), decodedChallenge),
        "challenge does not match");
    checkArgument(
        attributeValidator.equals(challenge.getOrigin(), clientData.getOrigin()),
        "invalid challenge origin");

    if (attributeValidator.isNotNull(clientData.getTopOrigin())) {
      // todo: (joshfischer) https://w3c.github.io/webauthn/#dom-collectedclientdata-toporigin
      throw new IllegalArgumentException("topOrigin not permitted");
    }
    final var hash =
        Hashing.sha256()
            .hashString(new String(request.getClientDataJSON()), StandardCharsets.UTF_8)
            .asBytes();

    CborMap cborMap = null;
    try {
      cborMap = CborMap.createFromCborByteArray(request.getAttestationObject());
      // Prints out the line `toString: 55799({"a":1,"b":[2,3]})`
      System.out.println("toString: " + cborMap);

      // Prints out the line `toJsonString: {"a":1,"b":[2,3]}`
      System.out.println("toJsonString: " + cborMap.toJsonString());
    } catch (CborParseException e) {
      throw new RuntimeException(e);
    }

    try {
      ByteBuffer signatureBase =
          ByteBuffer.allocate(request.getAuthenticatorData().length + hash.length)
              .put(request.getAuthenticatorData())
              .put(hash);

      KeyFactory kf = KeyFactory.getInstance("EC");
      final X509EncodedKeySpec keySpec = new X509EncodedKeySpec(request.getPublicKey());
      PublicKey publicKey = kf.generatePublic(keySpec);
      Signature sig = Signature.getInstance("SHA256withECDSA");
      sig.initVerify(publicKey);
      logger.info(
          "authenticator data: "
              + new String(request.getAuthenticatorData(), StandardCharsets.UTF_8));
      final var rpIdHash = Arrays.copyOfRange(request.getAuthenticatorData(), 0, 31);
      final var rpIdHashString = new String(rpIdHash, StandardCharsets.UTF_8);
      sig.update(signatureBase);
      // boolean isCorrect = sig.verify(signature);
    } catch (NoSuchAlgorithmException
        | InvalidKeySpecException
        | InvalidKeyException
        | SignatureException e) {
      throw new RuntimeException(e);
    }

    logger.info("key: ");
  }
}
