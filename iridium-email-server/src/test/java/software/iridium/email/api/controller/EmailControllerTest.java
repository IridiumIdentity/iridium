package software.iridium.email.api.controller;

import software.iridium.api.email.domain.EmailSendRequest;
import software.iridium.email.api.service.EmailService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class EmailControllerTest {

    @Mock
    private EmailService mockEmailService;
    @InjectMocks
    private EmailController subject;

    @Test
    public void send_AllGood_BehavesAsExpected() {
        final var request = new EmailSendRequest();

        subject.send(request);

        verify(mockEmailService).send(same(request));
    }
}
