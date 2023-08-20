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
package software.iridium.api.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;
import software.iridium.api.authentication.domain.ExternalProviderLoginDescriptorResponse;
import software.iridium.api.authentication.domain.LoginDescriptorResponse;
import software.iridium.api.util.SubdomainExtractor;

@ExtendWith(MockitoExtension.class)
class TemplateServiceTest {

  @Mock private SubdomainExtractor mockSubdomainExtractor;
  @Mock private LoginDescriptorService mockDescriptorService;
  @Mock private Model mockModel;
  @Mock private HttpServletRequest mockServletRequest;
  @InjectMocks private TemplateService subject;

  @AfterEach
  public void ensureNoUnexpectedMockInteractions() {
    Mockito.verifyNoMoreInteractions(
        mockSubdomainExtractor, mockDescriptorService, mockModel, mockServletRequest);
  }

  @Test
  @Disabled
  public void describeIndex_AllGood_BehavesAsExpected() {
    final var params = new HashMap<String, String>();
    final var subdomain = "somewhere";
    final var displayName = "the display name";
    final var tenantLogoUrl = "http://somewhere.com";
    final var externalDescriptors = new ArrayList<ExternalProviderLoginDescriptorResponse>();
    final var loginDescriptor = new LoginDescriptorResponse();
    loginDescriptor.setExternalProviderDescriptors(externalDescriptors);
    loginDescriptor.setDisplayName(displayName);
    loginDescriptor.setTenantLogoUrl(tenantLogoUrl);
    final var stringBuffer = new StringBuffer(subdomain);

    when(mockServletRequest.getRequestURL()).thenReturn(stringBuffer);
    when(mockSubdomainExtractor.extract(eq(subdomain))).thenReturn(subdomain);
    when(mockDescriptorService.getBySubdomain(subdomain)).thenReturn(loginDescriptor);

    assertThat(subject.describeIndex(mockModel, mockServletRequest, params), is(equalTo("index")));

    verify(mockServletRequest).getRequestURL();
    verify(mockSubdomainExtractor).extract(eq(subdomain));
    verify(mockModel).addAttribute(eq("displayName"), same(displayName));
    verify(mockModel).addAttribute(eq("externalProviderDescriptors"), same(externalDescriptors));
    verify(mockModel).addAttribute(eq("tenantLogoUrl"), same(tenantLogoUrl));
  }
}
