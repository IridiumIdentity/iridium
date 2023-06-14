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
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import software.iridium.api.authentication.domain.IdentitySummaryResponse;
import software.iridium.entity.IdentityEntity;

@Component
public class IdentitySummaryResponseMapper {
  public List<IdentitySummaryResponse> mapToSummaries(final List<IdentityEntity> entities) {
    if (CollectionUtils.isEmpty(entities)) {
      return Collections.emptyList();
    }
    return entities.stream().map(this::mapToSummary).collect(Collectors.toList());
  }

  public IdentitySummaryResponse mapToSummary(final IdentityEntity entity) {
    final var response = new IdentitySummaryResponse();
    response.setId(entity.getId());
    final var profile = entity.getProfile();

    if (profile != null) {
      response.setFirstName(profile.getFirstName());
      response.setLastName(profile.getLastName());
    }

    response.setEmailAddress(entity.getPrimaryEmail().getEmailAddress());

    return response;
  }
}
