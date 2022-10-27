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
package software.iridium.api.email.client;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;
import software.iridium.api.base.client.ErrorHandler;
import software.iridium.api.base.client.RestPoster;
import software.iridium.api.base.domain.ApiDataResponse;
import software.iridium.api.email.domain.EmailSendRequest;
import software.iridium.api.email.domain.EmailSendResponse;

public class EmailApiClient {

  private static final String EMAILS_PATH_FMT = "%semails";

  private String baseUrl;
  private RestTemplate restTemplate;

  public EmailApiClient(final String baseUrl, final RestTemplate restTemplate) {
    this.baseUrl = baseUrl;
    this.restTemplate = restTemplate;
  }

  public ApiDataResponse<EmailSendResponse> sendNewIdentityVerificationMail(
      final EmailSendRequest request) {
    final var restPoster =
        new RestPoster<EmailSendRequest, ApiDataResponse<EmailSendResponse>>(this.restTemplate);
    final var headers = new HttpHeaders();
    headers.set("Accept", EmailSendResponse.MEDIA_TYPE);
    headers.set("Content-Type", EmailSendRequest.MEDIA_TYPE);
    final var url = String.format(EMAILS_PATH_FMT, baseUrl);
    return restPoster.post(
        url,
        headers,
        request,
        new ParameterizedTypeReference<ApiDataResponse<EmailSendResponse>>() {},
        new ErrorHandler());
  }
}
