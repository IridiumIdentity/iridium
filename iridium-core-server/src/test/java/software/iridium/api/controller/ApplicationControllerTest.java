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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import software.iridium.api.authentication.domain.ApplicationCreateRequest;
import software.iridium.api.authentication.domain.ApplicationCreateResponse;
import software.iridium.api.service.ApplicationService;

@ExtendWith(MockitoExtension.class)
public class ApplicationControllerTest {

  @Mock private ApplicationService mockService;
  @InjectMocks private ApplicationController subject;

  @AfterEach
  public void ensureNoUnexpectedMockInteractions() {
    Mockito.verifyNoMoreInteractions(mockService);
  }

  @Test
  public void create_AllGood_BehavesAsExpected() {
    final var request = new ApplicationCreateRequest();
    final var appResponse = new ApplicationCreateResponse();
    final var tenantId = "the id";

    when(mockService.create(same(request), same(tenantId))).thenReturn(appResponse);

    final var response = subject.create(request, tenantId);

    verify(mockService).create(same(request), same(tenantId));

    assertThat(response, sameInstance(appResponse));
  }

  @Test
  public void getPageByTenantIdAndApplicationTypeAllGood_BehavesAsExpected() {
    final var orgId = "the org id";
    final var page = 1;
    final var size = 20;

    subject.getPageByTenantAndApplicationType(orgId, page, size, true);

    verify(mockService)
        .getPageByTenantId(
            same(orgId), same(page), same(size), eq(true));
  }
}
