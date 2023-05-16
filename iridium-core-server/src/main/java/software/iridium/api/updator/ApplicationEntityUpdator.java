package software.iridium.api.updator;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import software.iridium.api.authentication.domain.ApplicationUpdateRequest;
import software.iridium.api.entity.ApplicationEntity;
import software.iridium.api.entity.ApplicationTypeEntity;

@Component
public class ApplicationEntityUpdator {


    @Transactional(propagation = Propagation.REQUIRED)
    public ApplicationEntity update(final ApplicationEntity entity, final ApplicationTypeEntity applicationType, final ApplicationUpdateRequest request) {
        entity.setApplicationType(applicationType);
        entity.setRedirectUri(request.getRedirectUri());
        entity.setDescription(request.getDescription());
        entity.setName(request.getName());
        entity.setHomePageUrl(request.getHomePageUrl());
        entity.setPrivacyPolicyUrl(request.getPrivacyPolicyUrl());
        entity.setIconUrl(request.getIconUrl());
        return entity;
    }
}
