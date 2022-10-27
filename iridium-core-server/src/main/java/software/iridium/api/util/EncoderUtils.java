/*
 *  Copyright (C) Josh Fischer - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Josh Fischer <josh@joshfischer.io>, 2022.
 */

package software.iridium.api.util;

import org.apache.commons.codec.binary.Hex;
import org.springframework.stereotype.Component;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

@Component
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
