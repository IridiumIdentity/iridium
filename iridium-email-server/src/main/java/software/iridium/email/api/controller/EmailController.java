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

package software.iridium.email.api.controller;

import javax.annotation.Resource;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import software.iridium.api.base.domain.ApiDataResponse;
import software.iridium.api.email.domain.EmailSendRequest;
import software.iridium.api.email.domain.EmailSendResponse;
import software.iridium.email.api.service.EmailService;

@CrossOrigin
@RestController
public class EmailController {

  @Resource private EmailService emailService;

  @PostMapping(
      path = "/emails",
      consumes = EmailSendRequest.MEDIA_TYPE,
      produces = EmailSendResponse.MEDIA_TYPE)
  public ApiDataResponse<EmailSendResponse> send(@RequestBody final EmailSendRequest request) {
    // return new ApiDataResponse<>(emailService.send(request));
    return new ApiDataResponse<>(null);
  }
}
