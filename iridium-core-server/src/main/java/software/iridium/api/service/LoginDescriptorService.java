/*
 *  Copyright (C) Josh Fischer - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Josh Fischer <josh@joshfischer.io>, 2023.
 */
package software.iridium.api.service;

import static com.google.common.base.Preconditions.checkArgument;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import software.iridium.api.authentication.domain.LoginDescriptorCreateRequest;
import software.iridium.api.authentication.domain.LoginDescriptorResponse;
import software.iridium.api.base.error.ResourceNotFoundException;
import software.iridium.api.mapper.LoginDescriptorResponseMapper;
import software.iridium.api.repository.LoginDescriptorEntityRepository;
import software.iridium.api.util.AttributeValidator;

@Service
public class LoginDescriptorService {

  @Resource private LoginDescriptorEntityRepository loginDescriptorRepository;
  @Resource private LoginDescriptorResponseMapper responseMapper;
  @Resource private AttributeValidator attributeValidator;

  @Transactional(propagation = Propagation.SUPPORTS)
  public LoginDescriptorResponse getBySubdomain(final String subdomain) {
    checkArgument(attributeValidator.isValidSubdomain(subdomain), "subdomain must be valid format");

    final var loginDescriptor =
        loginDescriptorRepository
            .findByTenant_Subdomain(subdomain)
            .orElseThrow(
                () ->
                    new ResourceNotFoundException(
                        "login descriptor not found for subdomain: " + subdomain));

    return responseMapper.map(loginDescriptor);
  }

  public LoginDescriptorResponse create(
      final LoginDescriptorCreateRequest request, final String tenantId) {
    return null;
  }
}
