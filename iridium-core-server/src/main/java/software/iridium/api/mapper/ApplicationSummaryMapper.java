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

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import software.iridium.api.authentication.domain.ApplicationSummary;
import software.iridium.entity.ApplicationEntity;

@Component
public class ApplicationSummaryMapper {

  public List<ApplicationSummary> mapToSummaries(final Collection<ApplicationEntity> entities) {
    if (CollectionUtils.isEmpty(entities)) {
      return Collections.emptyList();
    }
    return entities.stream().map(this::mapToSummary).collect(Collectors.toList());
  }

  public ApplicationSummary mapToSummary(final ApplicationEntity entity) {
    final var response = new ApplicationSummary();
    response.setId(entity.getId());
    response.setName(entity.getName());
    response.setApplicationTypeId(entity.getApplicationType().getId());
    response.setClientId(entity.getClientId());
    return response;
  }
}
