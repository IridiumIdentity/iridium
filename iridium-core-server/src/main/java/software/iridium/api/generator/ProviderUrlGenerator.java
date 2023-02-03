/*
 *  Copyright (C) Josh Fischer - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Josh Fischer <josh@joshfischer.io>, 2023.
 */
package software.iridium.api.generator;

import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;
import software.iridium.api.entity.ExternalIdentityProviderEntity;

@Component
public class ProviderUrlGenerator {

  public String generate(final ExternalIdentityProviderEntity provider, final String code) {

    return UriComponentsBuilder.fromUriString(provider.getAccessTokenRequestBaseUrl())
        .queryParam("client_id", provider.getClientId())
        .queryParam("client_secret", provider.getClientSecret())
        .queryParam("code", code)
        .queryParam("redirect_url", provider.getRedirectUri())
        .buildAndExpand()
        .toUriString();
  }
}
