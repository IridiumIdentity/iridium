package software.iridium.api.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import software.iridium.api.authentication.domain.ApplicationUpdateRequest;
import software.iridium.api.util.AttributeValidator;

import static com.google.common.base.Preconditions.checkArgument;

@Component
public class ApplicationUpdateRequestValidator {

    @Autowired private AttributeValidator validator;

    public void validate(final ApplicationUpdateRequest request) {

        checkArgument(
                validator.isNotBlankAndNoLongerThan(request.getName(), 100),
                "name must not blank and no longer than 100 characters: " + request.getName());
        checkArgument(
                validator.isNotBlankAndNoLongerThan(request.getDescription(), 255),
                "description must not blank and no longer than 255 characters: " + request.getDescription());
        checkArgument(
                validator.isValidUrl(request.getHomePageUrl()),
                "homePageUrl must be a valid url: " + request.getHomePageUrl());
        checkArgument(
                validator.isValidUrl(request.getPrivacyPolicyUrl()),
                "privacyPolicyUrl must be a valid url: " + request.getPrivacyPolicyUrl());
        checkArgument(
                validator.isValidUrl(request.getRedirectUri()),
                "redirectUri must be a valid url: " + request.getRedirectUri());
        checkArgument(validator.isNotBlank(request.getIconUrl()), "iconUrl must not be blank: " + request.getIconUrl());
        checkArgument(validator.isUuid(request.getApplicationTypeId()), "applicationTypeId must not be a valid uuid: " + request.getApplicationTypeId());

    }
}
