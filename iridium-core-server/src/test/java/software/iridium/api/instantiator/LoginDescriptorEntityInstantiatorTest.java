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
package software.iridium.api.instantiator;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import software.iridium.entity.TenantEntity;

class LoginDescriptorEntityInstantiatorTest {

  private LoginDescriptorEntityInstantiator subject;

  @BeforeEach
  public void setUpForEachTestCase() {
    subject = new LoginDescriptorEntityInstantiator();
  }

  @Test
  public void instantiate_AllGood_BehavesAsExpected() {
    final var tenant = new TenantEntity();

    final var response = subject.instantiate(tenant);

    assertThat(response.getTenant(), sameInstance(tenant));
    assertThat(tenant.getLoginDescriptor(), sameInstance(response));
  }
}
