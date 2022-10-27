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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import software.iridium.api.authentication.domain.CreateTenantRequest;
import software.iridium.api.authentication.domain.TenantSummary;
import software.iridium.api.service.TenantService;

@ExtendWith(MockitoExtension.class)
class TenantControllerTest {

  @Mock private TenantService mockTenantService;
  @Mock private HttpServletRequest mockServletRequest;
  @InjectMocks private TenantController subject;

  @AfterEach
  public void ensureNoUnexpectedMockInteractions() {
    Mockito.verifyNoMoreInteractions(mockTenantService, mockServletRequest);
  }

  @Test
  public void getTenantSummaries_AllGood_BehavesAsExpected() {
    final var summaries = new ArrayList<TenantSummary>();

    when(mockTenantService.getTenantSummaries(same(mockServletRequest))).thenReturn(summaries);

    assertThat(subject.getTenantSummaries(mockServletRequest).getData(), sameInstance(summaries));

    verify(mockTenantService).getTenantSummaries(same(mockServletRequest));
  }

  @Test
  public void create_AllGood_BehavesAsExpected() {
    final var request = new CreateTenantRequest();

    subject.create(mockServletRequest, request);

    verify(mockTenantService).create(same(mockServletRequest), same(request));
  }
}
