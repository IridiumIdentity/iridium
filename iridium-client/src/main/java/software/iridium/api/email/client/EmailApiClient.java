/*
 *  Copyright (C) Josh Fischer - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Josh Fischer <josh@joshfischer.io>, 2023.
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
