package software.iridium.email.api.controller;

import software.iridium.api.base.domain.ApiDataResponse;
import software.iridium.api.email.domain.EmailTemplateResponse;
import software.iridium.email.api.service.EmailTemplateService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@CrossOrigin
@RestController
public class EmailTemplateController {

    @Resource
    private EmailTemplateService templateService;

    @GetMapping(value = "/email-templates/{email-template-id}", produces = EmailTemplateResponse.MEDIA_TYPE)
    public ApiDataResponse<EmailTemplateResponse> get(final String emailTemplateId) {
        return new ApiDataResponse<>(templateService.get(emailTemplateId));
    }
}

