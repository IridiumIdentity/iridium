package software.iridium.email.api.mapper;

import software.iridium.api.email.domain.EmailTemplateResponse;
import software.iridium.email.api.entity.EmailTemplateEntity;
import org.springframework.stereotype.Component;

@Component
public class EmailTemplateResponseMapper {

    public EmailTemplateResponse map(final EmailTemplateEntity entity) {
        final var response = new EmailTemplateResponse();
        response.setId(entity.getId());
        response.setTenantId(entity.getTenantId());
        response.setFilePath(entity.getFilePath());
        response.setContent("some content to set");
        return response;
    }
}
