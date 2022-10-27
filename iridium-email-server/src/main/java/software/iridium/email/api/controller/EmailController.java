package software.iridium.email.api.controller;

import software.iridium.api.base.domain.ApiDataResponse;
import software.iridium.api.email.domain.EmailSendRequest;
import software.iridium.api.email.domain.EmailSendResponse;
import software.iridium.email.api.service.EmailService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@CrossOrigin
@RestController
public class EmailController {

    @Resource
    private EmailService emailService;

    @PostMapping(path = "/emails", consumes = EmailSendRequest.MEDIA_TYPE, produces = EmailSendResponse.MEDIA_TYPE)
    public ApiDataResponse<EmailSendResponse> send(@RequestBody final EmailSendRequest request) {
        return new ApiDataResponse<>(emailService.send(request));
    }
}
