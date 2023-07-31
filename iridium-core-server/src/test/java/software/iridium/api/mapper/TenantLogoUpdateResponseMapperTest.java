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
package software.iridium.api.mapper;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import software.iridium.entity.LoginDescriptorEntity;
import software.iridium.entity.TenantEntity;

class TenantLogoUpdateResponseMapperTest {

  private TenantLogoUpdateResponseMapper subject;

  @BeforeEach
  public void setUpForEachTestCase() {
    subject = new TenantLogoUpdateResponseMapper();
  }

  @Test
  public void map_AllGood_BehavesAsExpected() {
    final var iconUrl = "the url";
    final var loginDescriptorId = "the id";
    final var tenantId = "the tenant id";
    final var tenant = new TenantEntity();
    tenant.setId(tenantId);
    final var descriptor = new LoginDescriptorEntity();
    descriptor.setId(loginDescriptorId);
    descriptor.setLogoURL(iconUrl);
    descriptor.setTenant(tenant);

    final var response = subject.map(descriptor);

    assertThat(response.getTenantId(), is(equalTo(tenantId)));
    assertThat(response.getLogoUrl(), is(equalTo(iconUrl)));
    assertThat(response.getLoginDescriptorId(), is(equalTo(loginDescriptorId)));
  }
}
