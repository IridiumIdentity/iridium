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
package software.iridium.tracker.mapper;

import java.util.*;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import software.iridium.api.tracker.AggregateProviderSummary;
import software.iridium.entity.IdentityEntity;

@Component
public class ProviderCountResponseMapper {
  @Transactional(propagation = Propagation.SUPPORTS)
  public List<AggregateProviderSummary> map(final List<IdentityEntity> identities) {
    if (CollectionUtils.isEmpty(identities)) {
      return Collections.emptyList();
    }

    Map<String, Integer> providerCountMap = new HashMap<>();
    for (IdentityEntity identity : identities) {
      String providerName = identity.getProvider().getDisplayName();
      providerCountMap.put(providerName, providerCountMap.getOrDefault(providerName, 0) + 1);
    }

    final var summaries = new ArrayList<AggregateProviderSummary>();
    for (Map.Entry<String, Integer> entry : providerCountMap.entrySet()) {
      summaries.add(AggregateProviderSummary.of(entry.getKey(), entry.getValue()));
    }
    return summaries;
  }
}
