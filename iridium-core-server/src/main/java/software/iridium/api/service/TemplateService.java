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
package software.iridium.api.service;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import software.iridium.api.authentication.domain.CreatePasskeyRequest;
import software.iridium.api.base.error.ResourceNotFoundException;
import software.iridium.api.instantiator.ChallengeEntityInstantiator;
import software.iridium.api.repository.ChallengeEntityRepository;
import software.iridium.api.repository.TenantEntityRepository;
import software.iridium.api.util.SubdomainExtractor;
import software.iridium.entity.ChallengeType;

@Service
public class TemplateService {

  @Autowired private SubdomainExtractor subdomainExtractor;
  @Autowired private LoginDescriptorService loginDescriptorService;
  @Autowired private TenantEntityRepository tenantRepository;
  @Autowired private ChallengeEntityInstantiator challengeInstantiator;
  @Autowired private ChallengeEntityRepository challengeRepository;

  @Transactional(propagation = Propagation.REQUIRED)
  public String describeIndex(
      final Model model,
      final HttpServletRequest servletRequest,
      final Map<String, String> params) {

    final var subdomain = subdomainExtractor.extract(servletRequest.getRequestURL().toString());

    final var loginDescriptor = loginDescriptorService.getBySubdomain(subdomain);

    final var tenant =
        tenantRepository
            .findBySubdomain(subdomain)
            .orElseThrow(() -> new ResourceNotFoundException("tenant not found"));

    final var registrationChallenge =
        challengeRepository.save(challengeInstantiator.instantiate(tenant, ChallengeType.CREATION));

    model.addAttribute("displayName", loginDescriptor.getDisplayName());
    model.addAttribute("tenantLogoUrl", loginDescriptor.getTenantLogoUrl());
    model.addAttribute("registrationChallenge", registrationChallenge.getChallenge());
    model.addAttribute("createPasskeyRequest", new CreatePasskeyRequest());
    model.addAttribute(
        "externalProviderDescriptors", loginDescriptor.getExternalProviderDescriptors());

    return "index";
  }
}
