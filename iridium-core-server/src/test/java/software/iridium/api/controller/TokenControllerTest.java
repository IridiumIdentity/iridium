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
package software.iridium.api.controller;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.verify;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import software.iridium.api.service.TokenManager;

@ExtendWith(MockitoExtension.class)
class TokenControllerTest {

  @Mock private TokenManager mockManager;
  @Mock private HttpServletRequest mockServletRequest;
  @Mock private HttpServletResponse mockServletResponse;
  @InjectMocks private TokenController subject;

  @AfterEach
  public void ensureNoUnexpectedMockInteractions() {
    Mockito.verifyNoMoreInteractions(mockManager, mockServletRequest, mockServletResponse);
  }

  @Test
  public void logout_AllGood_BehavesAsExpected() {
    final var clientId = "the clientId";

    subject.logout(clientId, mockServletRequest, mockServletResponse);

    verify(mockManager).logout(same(clientId), same(mockServletRequest));
    verify(mockServletResponse).setStatus(eq(HttpServletResponse.SC_NO_CONTENT));
  }
}
