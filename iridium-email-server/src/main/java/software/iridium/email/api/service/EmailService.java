package software.iridium.email.api.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import software.iridium.api.email.domain.EmailSendRequest;
import software.iridium.api.email.domain.EmailSendResponse;

import javax.annotation.Resource;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;

@Service
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    private static final List<String> templates = List.of(
            "initiate-password-reset",
            "confirm-password-reset",
            "new-user-verification",
            "email-verification",
            "update-user-verification",
            "new-identity");
    @Resource
    private EmailSender emailSender;


    @Transactional(propagation = Propagation.REQUIRED)
    public EmailSendResponse send(final EmailSendRequest request)  {
        checkArgument(templates.contains(request.getTemplate()), "incorrect email type, " + request.getTemplate() + " : email templates supported are: ", templates);
        // validate the request
        try {
            emailSender.send(request);
        } catch (Exception e) {
            logger.error("failed to send mail to {} ", request.getTo(), e);
            throw new RuntimeException("boom", e);
        }
        return null;
    }
}


