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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.sameInstance;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.*;

import javax.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import software.iridium.api.authentication.domain.IdentityResponse;
import software.iridium.api.service.IdentityService;

@ExtendWith(MockitoExtension.class)
class IdentityControllerTest {

  @Mock private IdentityService mockIdentityService;
  @Mock private HttpServletRequest mockServletRequest;
  @InjectMocks private IdentityController subject;

  @AfterEach
  public void verifyNoUnexpectedMockInteractions() {
    Mockito.verifyNoMoreInteractions(mockIdentityService);
  }

  @Test
  public void getIdentity_AllGood_BehavesAsExpected() {
    final var response = new IdentityResponse();

    when(mockIdentityService.getIdentity(same(mockServletRequest))).thenReturn(response);

    assertThat(subject.getIdentity(mockServletRequest).getData(), sameInstance(response));

    verify(mockIdentityService).getIdentity(same(mockServletRequest));
  }
}
