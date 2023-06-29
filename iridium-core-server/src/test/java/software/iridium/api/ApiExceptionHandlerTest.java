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
package software.iridium.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import software.iridium.api.base.error.BadRequestException;
import software.iridium.api.base.error.DuplicateResourceException;
import software.iridium.api.base.error.NotAuthorizedException;
import software.iridium.api.base.error.ResourceNotFoundException;

@WebMvcTest
@ContextConfiguration(classes = ApiExceptionHandler.class)
public class ApiExceptionHandlerTest {

  private MockMvc mockMvc;

  @RestController
  @RequestMapping("/tests")
  public static class RestControllerThrowingException {
    @GetMapping(value = "/illegalargument")
    public void throwIllegalArgumentException() {
      throw new IllegalArgumentException();
    }

    @GetMapping(value = "/notauthorized")
    public void throwNotAuthorizedException() {
      throw new NotAuthorizedException("Unauthorized Access");
    }

    @GetMapping(value = "/conflict")
    public void throwDuplicateResourceException() {
      throw new DuplicateResourceException("Conflict detected");
    }

    @GetMapping(value = "/notfound")
    public void throwResourceNotFoundException() {
      throw new ResourceNotFoundException("Resource not found");
    }

    @GetMapping(value = "/badrequest")
    public void throwBadRequestException() {
      throw new BadRequestException("Bad request");
    }
  }

  @BeforeEach
  public void setup() {
    this.mockMvc =
        MockMvcBuilders.standaloneSetup(new RestControllerThrowingException())
            .setControllerAdvice(new ApiExceptionHandler())
            .build();
  }

  @Test
  public void handleValidationException_AllGood_ExceptionHandledProperly() throws Exception {
    mockMvc.perform(get("/tests/illegalargument")).andExpect(status().isBadRequest());
  }

  @Test
  public void handleNotAuthorizedException_AcceptJSON_ExceptionHandledProperly_And_JSONReturned()
      throws Exception {
    mockMvc
        .perform(get("/tests/notauthorized").accept(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isUnauthorized())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE));
  }

  @Test
  public void
      handleNotAuthorizedException_AllGood_ExceptionHandledProperly_And_HTMLErrorPageReturned()
          throws Exception {
    mockMvc
        .perform(get("/tests/notauthorized"))
        .andExpect(status().isUnauthorized())
        .andExpect(view().name("error"))
        .andExpect(model().attribute("statusCode", String.valueOf(HttpStatus.UNAUTHORIZED.value())))
        .andExpect(model().attributeExists("errorMessage"));
  }

  @Test
  public void handleDuplicateResourceException_AllGood_ExceptionHandledProperly() throws Exception {
    mockMvc.perform(get("/tests/conflict")).andExpect(status().isConflict());
  }

  @Test
  public void handleResourceNotFoundException_AllGood_ExceptionHandledProperly() throws Exception {
    mockMvc.perform(get("/tests/notfound")).andExpect(status().isNotFound());
  }

  @Test
  public void handleAccessTokenBadRequest_AllGood_ExceptionHandledProperly() throws Exception {
    mockMvc.perform(get("/tests/badrequest")).andExpect(status().isBadRequest());
  }
}
