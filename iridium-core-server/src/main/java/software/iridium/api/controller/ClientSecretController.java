/*
 *  Copyright (C) Josh Fischer - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Josh Fischer <josh@joshfischer.io>, 2022.
 */

package software.iridium.api.controller;

import software.iridium.api.base.domain.ApiDataResponse;
import software.iridium.api.base.domain.ApiResponse;
import software.iridium.api.authentication.domain.ClientSecretCreateResponse;
import software.iridium.api.service.ClientSecretService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@CrossOrigin
@RestController
public class ClientSecretController {

    @Resource
    private ClientSecretService clientSecretService;

    @PostMapping(value = "applications/{application-id}/client-secrets", produces = ClientSecretCreateResponse.MEDIA_TYPE)
    public ApiDataResponse<ClientSecretCreateResponse> create(@PathVariable(name = "application-id") final String applicationId) {

        return new ApiDataResponse<>(clientSecretService.create(applicationId));
    }

    @DeleteMapping(value = "applications/{application-id}/client-secrets/{client-secret-id}")
    public ApiResponse delete(@PathVariable(name = "application-id") final String applicationId,
                              @PathVariable(name = "client-secret-id") final String clientSecretId) {
        clientSecretService.delete(applicationId, clientSecretId);
        return new ApiResponse();
    }
}
