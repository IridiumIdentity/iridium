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

import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;

@ExtendWith(MockitoExtension.class)
class ServletTokenExtractorTest {

  @Mock private HttpServletRequest mockServletRequest;
  @InjectMocks private ServletTokenExtractor subject;

  @AfterEach
  public void ensureNoMockInteractions() {
    Mockito.verifyNoMoreInteractions(mockServletRequest);
  }

  @Test
  public void extract_AllGood_ExtractsAsExpected() {
    final var headerValue = "Bearer someTokenValue";

    when(mockServletRequest.getHeader(same(HttpHeaders.AUTHORIZATION))).thenReturn(headerValue);

    final var response = subject.extractBearerToken(mockServletRequest);

    verify(mockServletRequest).getHeader(same(HttpHeaders.AUTHORIZATION));
  }
}
