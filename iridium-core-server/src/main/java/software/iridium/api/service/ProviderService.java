/*
 *  Copyright (C) Josh Fischer - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Josh Fischer <josh@joshfischer.io>, 2023.
 */
package software.iridium.api.service;

import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import software.iridium.api.authentication.domain.ProviderSummaryResponse;
import software.iridium.api.mapper.ProviderSummaryResponseMapper;
import software.iridium.api.repository.ExternalIdentityProviderTemplateEntityRepository;

@Service
public class ProviderService {

  @Resource private ExternalIdentityProviderTemplateEntityRepository providerRepository;
  @Resource private ProviderSummaryResponseMapper responseMapper;

  @Transactional(propagation = Propagation.SUPPORTS)
  public List<ProviderSummaryResponse> retrieveAllSummaries() {

    return responseMapper.mapList(providerRepository.findAll());
  }
}
