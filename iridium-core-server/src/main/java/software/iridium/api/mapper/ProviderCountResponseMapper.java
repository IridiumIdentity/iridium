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

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import software.iridium.entity.IdentityEntity;

public class ProviderCountResponseMapper {

  @Transactional(propagation = Propagation.SUPPORTS)
  public Map<String, Integer> getMap(final List<IdentityEntity> identities) {
    if (CollectionUtils.isEmpty(identities)) {
      return Collections.emptyMap();
    }

    Map<String, Integer> providerCountMap = new HashMap<>();
    for (IdentityEntity identity : identities) {
      String providerName = identity.getProvider().getName();
      providerCountMap.put(providerName, providerCountMap.getOrDefault(providerName, 0) + 1);
    }

    return providerCountMap;
  }
}
