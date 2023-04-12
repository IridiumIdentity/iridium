/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package software.iridium.api.util;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MimeMessageHelperInstantiatorTest {

  @Mock private MimeMessage mockMimeMessage;
  @InjectMocks private MimeMessageHelperInstantiator subject;

  @Test
  public void instantiate_AllGood_BehavesAsExpected() throws MessagingException {

    final var response = subject.instantiate(mockMimeMessage);

    assertNotNull(response);
    assertThat(response.getEncoding(), is(equalTo(StandardCharsets.UTF_8.name())));
    assertThat(response.getMimeMessage(), is(equalTo(mockMimeMessage)));
  }
}
