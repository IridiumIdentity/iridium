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

package software.iridium.api.generator;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.util.LinkedMultiValueMap;

class RedirectUrlGeneratorTest {

  private RedirectUrlGenerator subject;

  @BeforeEach
  public void setupForEachTestCase() {
    subject = new RedirectUrlGenerator();
  }

  @Test
  public void generate_AllGood_GeneratesAsExpected() {
    final var baseUrl = "https://somewhere.com";
    final var paramMap = new LinkedMultiValueMap<String, String>();
    paramMap.put("param1", Arrays.asList("one"));
    paramMap.put("param2", Arrays.asList("two"));

    final var response = subject.generate(baseUrl, paramMap);

    assertThat(response, is(equalTo("https://somewhere.com?param1=one&param2=two")));
  }
}
