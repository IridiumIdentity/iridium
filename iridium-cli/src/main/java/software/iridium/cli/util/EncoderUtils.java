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
package software.iridium.cli.util;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import org.apache.commons.codec.binary.Hex;

public class EncoderUtils {

  public String cryptoSecureToHex(final Integer length) throws NoSuchAlgorithmException {
    byte[] bytes = new byte[length];
    SecureRandom.getInstanceStrong().nextBytes(bytes);
    return Hex.encodeHexString(bytes);
  }

  public String generateCryptoSecureString(final Integer length) {
    String characterChoices = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";

    StringBuilder sb = new StringBuilder();
    final var secureRandom = new SecureRandom();
    for (int i = 0; i < length; i++) {
      int index = (int) (secureRandom.nextDouble() * characterChoices.length());
      sb.append(characterChoices.charAt(index));
    }
    return sb.toString();
  }
}
