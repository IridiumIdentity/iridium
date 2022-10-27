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

import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import software.iridium.api.authentication.domain.InitiatePasswordResetRequest;
import software.iridium.api.authentication.domain.PasswordResetRequest;
import software.iridium.api.service.PasswordService;

@ExtendWith(MockitoExtension.class)
class PasswordControllerTest {

  @Mock private PasswordService mockPasswordService;
  @InjectMocks private PasswordController subject;

  @AfterEach
  public void ensureNoUnexpectedMockInteractions() {
    verifyNoMoreInteractions(mockPasswordService);
  }

  @Test
  public void initiateForgotPassword_AllGood_BehavesAsExpected() {
    final var request = new InitiatePasswordResetRequest();

    subject.initiateResetPassword(request);

    verify(mockPasswordService).initiatePasswordReset(same(request));
  }

  @Test
  public void resetPassword_AllGood_BehavesAsExpected() {
    final var request = new PasswordResetRequest();

    subject.resetPassword(request);

    verify(mockPasswordService).resetPassword(same(request));
  }
}
