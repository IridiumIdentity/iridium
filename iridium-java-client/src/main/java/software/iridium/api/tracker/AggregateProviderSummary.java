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
package software.iridium.api.tracker;

public class AggregateProviderSummary {

  private String name;

  private Integer value;

  private AggregateProviderSummary(final String name, final Integer count) {
    this.name = name;
    this.value = count;
  }

  public String getName() {
    return name;
  }

  public Integer getValue() {
    return value;
  }

  public static AggregateProviderSummary of(final String name, final Integer count) {
    return new AggregateProviderSummary(name, count);
  }
}
