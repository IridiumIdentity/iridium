/*
 *  Copyright (C) Josh Fischer - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Josh Fischer <josh@joshfischer.io>, 2022.
 */

package software.iridium.api.generator;

import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;


@Component
public class RedirectUrlGenerator {

    public String generate(final String redirectBaseUrl, final MultiValueMap<String, String> params) {
        return UriComponentsBuilder.fromUriString(redirectBaseUrl).queryParams(params).buildAndExpand().toUriString();
    }

}
