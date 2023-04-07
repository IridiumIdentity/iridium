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

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import software.iridium.api.service.LoginDescriptorService;

@ExtendWith(MockitoExtension.class)
class LoginDescriptorControllerTest {

  @Mock private LoginDescriptorService mockDescriptorService;
  @InjectMocks private LoginDescriptorController subject;

  @Test
  public void get_AllGood_BehavesAsExpected() {
    final var clientId = "the client id";

    subject.getBySubdomain(clientId);

    verify(mockDescriptorService).getBySubdomain(same(clientId));
  }
}
