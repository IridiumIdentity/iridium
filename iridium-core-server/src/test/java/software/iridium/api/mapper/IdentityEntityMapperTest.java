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

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import software.iridium.entity.IdentityEmailEntity;
import software.iridium.entity.IdentityEntity;
import software.iridium.entity.RoleEntity;
import software.iridium.entity.TenantEntity;

class IdentityEntityMapperTest {

  private IdentityEntityMapper subject;

  @BeforeEach
  public void setUpForEachTestCase() {
    subject = new IdentityEntityMapper();
  }

  @Test
  public void map_AllGood_MapsAsExpected() {
    final var id = "someId";
    final var emailAddress = "someUserName@nowhere.com";
    final var roleName = "roleName";
    final var tenantId = "the id";
    final var tenant = new TenantEntity();
    tenant.setId(tenantId);
    final var role = new RoleEntity();
    role.setName(roleName);
    final var entity = new IdentityEntity();
    final var email = new IdentityEmailEntity();
    email.setEmailAddress(emailAddress);
    email.setPrimary(true);
    entity.setId(id);
    entity.getEmails().add(email);
    entity.getRoles().add(role);
    entity.getManagedTenants().add(tenant);

    final var response = subject.map(entity);

    MatcherAssert.assertThat(response.getId(), is(equalTo(id)));
    MatcherAssert.assertThat(response.getUsername(), is(equalTo(emailAddress)));
    MatcherAssert.assertThat(response.getRoles().size(), is(equalTo(1)));
    MatcherAssert.assertThat(response.getTenantIds().size(), is(equalTo(1)));
    for (String domainRoleName : response.getRoles()) {
      assertThat(domainRoleName, is(equalTo(roleName)));
    }

    for (String tenantIdResponse : response.getTenantIds()) {
      assertThat(tenantIdResponse, is(equalTo(tenantId)));
    }
  }

  @Test
  public void map_EntityIsNull_ReturnsNull() {

    final var response = subject.map(null);

    assertThat(response, nullValue());
  }
}
