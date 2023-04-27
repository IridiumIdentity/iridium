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
package software.iridium.api.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import software.iridium.api.authentication.domain.InitiatePasswordResetRequest;
import software.iridium.api.authentication.domain.PasswordResetRequest;
import software.iridium.api.service.PasswordService;

@CrossOrigin
@RestController
public class PasswordController {

  private static final Logger logger = LoggerFactory.getLogger(PasswordController.class);

  @Autowired private PasswordService passwordService;

  @PostMapping(
      value = "/identities/initiate-reset-password",
      consumes = InitiatePasswordResetRequest.MEDIA_TYPE)
  public void initiateResetPassword(
      @RequestBody final InitiatePasswordResetRequest forgotPasswordRequest) {
    final var initiated = passwordService.initiatePasswordReset(forgotPasswordRequest);
    if (!initiated) {
      logger.info(
          "failed attempt to initiate forgot password {}", forgotPasswordRequest.getUsername());
    }
  }

  @PostMapping(value = "/reset-password", consumes = PasswordResetRequest.MEDIA_TYPE)
  public void resetPassword(@RequestBody PasswordResetRequest request) {
    passwordService.resetPassword(request);
  }

  @PostMapping(value = "/reset-password")
  public RedirectView resetWithFormSubmit(
      @ModelAttribute final PasswordResetRequest request,
      final ModelMap model,
      final RedirectAttributes redirectAttributes) {
    ServletRequestAttributes attr =
        (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
    final var session = attr.getRequest().getSession(true);
    final var view = passwordService.resetPassword(request);
    return new RedirectView(view, true);
  }
}
