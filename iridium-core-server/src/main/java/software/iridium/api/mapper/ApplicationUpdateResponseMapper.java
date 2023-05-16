package software.iridium.api.mapper;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import software.iridium.api.authentication.domain.ApplicationUpdateResponse;
import software.iridium.api.entity.ApplicationEntity;

@Component
public class ApplicationUpdateResponseMapper {

    @Transactional(propagation = Propagation.REQUIRED)
    public ApplicationUpdateResponse map(final ApplicationEntity entity) {
        final var response = new ApplicationUpdateResponse();
        response.setName(entity.getName());
        response.setApplicationTypeId(entity.getApplicationType().getId());
        response.setTenantId(entity.getTenantId());
        response.setId(entity.getId());
        response.setDescription(entity.getDescription());
        response.setCallbackURL(entity.getRedirectUri());
        response.setHomepageURL(entity.getHomePageUrl());
        response.setPrivacyPolicyUrl(entity.getPrivacyPolicyUrl());
        return response;
    }
}
