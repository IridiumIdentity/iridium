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

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import software.iridium.api.authentication.domain.CreateTenantRequest;
import software.iridium.api.authentication.domain.Environment;

class TenantInstantiatorTest {

  private TenantInstantiator subject;

  @BeforeEach
  public void setupForEachTestCase() {
    subject = new TenantInstantiator();
  }

  @Test
  public void instantiate_AllGood_BehavesAsExpected() {
    final var subdomain = "the subdomain";
    final var environment = Environment.DEVELOPMENT;
    final var request = new CreateTenantRequest();
    request.setSubdomain(subdomain);
    request.setEnvironment(environment);

    final var response = subject.instantiate(request);

    MatcherAssert.assertThat(response.getSubdomain(), is(equalTo(subdomain)));
    MatcherAssert.assertThat(response.getEnvironment(), is(equalTo(environment)));
  }
}
