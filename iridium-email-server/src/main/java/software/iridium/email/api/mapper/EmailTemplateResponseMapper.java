/*
 *  Copyright (C) Josh Fischer - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Josh Fischer <josh@joshfischer.io>, 2023.
 */
package software.iridium.email.api.mapper;

import org.springframework.stereotype.Component;
import software.iridium.api.email.domain.EmailTemplateResponse;
import software.iridium.email.api.entity.EmailTemplateEntity;

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
