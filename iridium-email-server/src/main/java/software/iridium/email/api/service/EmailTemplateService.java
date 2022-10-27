package software.iridium.email.api.service;

import software.iridium.api.base.error.ResourceNotFoundException;
import software.iridium.api.email.domain.EmailTemplateResponse;
import software.iridium.email.api.mapper.EmailTemplateResponseMapper;
import software.iridium.email.api.repository.EmailTemplateRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class EmailTemplateService {

    @Resource
    private EmailTemplateRepository templateRepository;
    @Resource
    private EmailTemplateResponseMapper responseMapper;


    public EmailTemplateResponse get(final String templateId) {
        final var entity = templateRepository
                .findById(templateId).orElseThrow(
                        () -> new ResourceNotFoundException("email template not found for id: " + templateId)
                );
        return responseMapper.map(entity);
    }
}
