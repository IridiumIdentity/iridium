/*
 *  Copyright (C) Josh Fischer - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Josh Fischer <josh@joshfischer.io>, 2023.
 */
package software.iridium.email.api.service;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import software.iridium.api.base.error.ResourceNotFoundException;
import software.iridium.api.email.domain.EmailTemplateResponse;
import software.iridium.email.api.mapper.EmailTemplateResponseMapper;
import software.iridium.email.api.repository.EmailTemplateRepository;

@Service
public class EmailTemplateService {

  @Resource private EmailTemplateRepository templateRepository;
  @Resource private EmailTemplateResponseMapper responseMapper;

  public EmailTemplateResponse get(final String templateId) {
    final var entity =
        templateRepository
            .findById(templateId)
            .orElseThrow(
                () ->
                    new ResourceNotFoundException(
                        "email template not found for id: " + templateId));
    return responseMapper.map(entity);
  }
}
