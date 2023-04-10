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
package software.iridium.email.api.mapper;

import org.springframework.stereotype.Component;
import software.iridium.api.email.domain.EmailTemplateResponse;
import software.iridium.email.api.entity.EmailTemplateEntity;

@Component
public class EmailTemplateResponseMapper {

  public EmailTemplateResponse map(final EmailTemplateEntity entity) {
    final var response = new EmailTemplateResponse();
    response.setId(entity.getId());
    response.setTenantId(entity.getTenantId());
    response.setFilePath(entity.getFilePath());
    response.setContent("some content to set");
    return response;
  }
}
