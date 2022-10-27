/*
 *  Copyright (C) Josh Fischer - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Josh Fischer <josh@joshfischer.io>, 2022.
 */

package software.iridium.api.validator;

import software.iridium.api.base.error.BadRequestException;
import software.iridium.api.util.AttributeValidator;
import software.iridium.api.entity.ApplicationEntity;
import software.iridium.api.generator.RedirectUrlGenerator;
import software.iridium.api.util.AuthorizationCodeFlowConstants;
import software.iridium.api.util.AuthorizationErrorKeys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;


@Component
public class AccessTokenRequestParameterValidator {
    // todo:  verify these error are thrown correctly, maybe thrown error descriptions as well
    private static final Logger logger = LoggerFactory.getLogger(AccessTokenRequestParameterValidator.class);
    @Resource
    private AttributeValidator attributeValidator;
    @Resource
    private RedirectUrlGenerator redirectUrlGenerator;

    public String validateAndOptionallyRedirect(final ApplicationEntity application, final Map<String, String> params) {

        if (attributeValidator.doesNotEqual(AuthorizationCodeFlowConstants.AUTHORIZATION_CODE_GRANT_TYPE.getValue(), params.getOrDefault(AuthorizationCodeFlowConstants.GRANT_TYPE.getValue(), ""))) {
            logger.info("grant type mismatch: {}", params.getOrDefault(AuthorizationCodeFlowConstants.GRANT_TYPE.getValue(), ""));
            final var detailErrorMap = new LinkedMultiValueMap<String, String>();
            detailErrorMap.put("error", List.of(AuthorizationErrorKeys.INVALID_REQUEST.getKey()));

            return redirectUrlGenerator.generate(application.getRedirectUri(), detailErrorMap);
        }

        if (application.getApplicationType().requiresSecret()) {
            final var secret = params.getOrDefault(AuthorizationCodeFlowConstants.CLIENT_SECRET.getValue(), "");

            logger.info("application requires secret");
            final var foundSecret = application.getClientSecrets().stream()
                    .filter(clientSecret -> secret.equals(clientSecret.getSecretKey()))
                    .findAny()
                    .orElse(null);

            if (foundSecret == null) {
                logger.info("application secret not found");
                throw new BadRequestException("application secret not found");
            }
        }

        if (attributeValidator.isBlank(params.getOrDefault(AuthorizationCodeFlowConstants.CODE_VERIFIER.getValue(), ""))) {
            logger.info("code_verifier is blank");
            throw new BadRequestException("code_verifier is blank");
        }

        if (attributeValidator.isBlank(params.getOrDefault(AuthorizationCodeFlowConstants.AUTHORIZATION_CODE.getValue(), ""))) {
            logger.info("authorization code is blank");
            throw new BadRequestException("authorization code is blank");
        }
        return null;
    }
}
