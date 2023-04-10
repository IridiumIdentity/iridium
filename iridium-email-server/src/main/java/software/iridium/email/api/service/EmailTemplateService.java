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
package software.iridium.email.api.service;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import software.iridium.api.base.error.ResourceNotFoundException;
import software.iridium.api.email.domain.EmailTemplateResponse;
import software.iridium.email.api.mapper.EmailTemplateResponseMapper;
import software.iridium.email.api.repository.EmailTemplateRepository;

@Service
public class EmailTemplateService {

  @Resource private EmailTemplateRepository templateRepository;
  @Resource private EmailTemplateResponseMapper responseMapper;

  public EmailTemplateResponse get(final String templateId) {
    final var entity =
        templateRepository
            .findById(templateId)
            .orElseThrow(
                () ->
                    new ResourceNotFoundException(
                        "email template not found for id: " + templateId));
    return responseMapper.map(entity);
  }
}
