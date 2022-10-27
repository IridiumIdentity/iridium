package software.iridium.email.api.controller;

import software.iridium.email.api.service.EmailTemplateService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class EmailTemplatesControllerTest {

    @Mock
    private EmailTemplateService mockTemplateService;
    @InjectMocks
    private EmailTemplateController subject;

    @AfterEach
    public void ensureNoUnexpectedMockInteractions() {
        Mockito.verifyNoMoreInteractions(mockTemplateService);
    }

    @Test
    public void get_AllGood_BehavesAsExpected() {
        final String id = "the id";

        subject.get(id);

        verify(mockTemplateService).get(same(id));
    }
}
