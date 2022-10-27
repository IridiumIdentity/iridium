/*
 *  Copyright (C) Josh Fischer - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Josh Fischer <josh@joshfischer.io>, 2022.
 */

package software.iridium.api.service;

import software.iridium.api.base.error.DuplicateResourceException;
import software.iridium.api.base.error.ResourceNotFoundException;
import software.iridium.api.instantiator.LoginDescriptorEntityInstantiator;
import software.iridium.api.instantiator.TenantInstantiator;
import software.iridium.api.repository.IdentityEntityRepository;
import software.iridium.api.repository.TenantEntityRepository;
import software.iridium.api.util.AttributeValidator;
import software.iridium.api.authentication.domain.CreateTenantRequest;
import software.iridium.api.authentication.domain.CreateTenantResponse;
import software.iridium.api.authentication.domain.TenantSummary;
import software.iridium.api.mapper.CreateTenantResponseMapper;
import software.iridium.api.mapper.TenantSummaryMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;

@Service
public class TenantService {

    @Resource
    private IdentityService identityService;
    @Resource
    private TenantEntityRepository tenantRepository;
    @Resource
    private TenantSummaryMapper summaryMapper;
    @Resource
    private AttributeValidator attributeValidator;
    @Resource
    private TenantInstantiator tenantInstantiator;
    @Resource
    private IdentityEntityRepository identityRepository;
    @Resource
    private CreateTenantResponseMapper responseMapper;
    @Resource
    private LoginDescriptorEntityInstantiator loginDescriptorInstantiator;

    @Transactional(propagation = Propagation.SUPPORTS)
    public List<TenantSummary> getTenantSummaries(final HttpServletRequest request) {

        final var identity = identityService.getIdentity(request);
        final var tenants = tenantRepository.findByIdIn(identity.getTenantIds())
                .orElseThrow(() -> new ResourceNotFoundException("one or more tenants not found for id(s) " + identity.getTenantIds()));
        return summaryMapper.mapToList(tenants);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public CreateTenantResponse create(final HttpServletRequest servletRequest, final CreateTenantRequest request) {
        checkArgument(attributeValidator.isValidSubdomain(request.getSubdomain()),"subdomain must only contains alphanumeric characters and hyphens");
        checkArgument(attributeValidator.isNotBlankAndNoLongerThan(request.getSubdomain(), 100), "subdomain must not be blank and no longer than 100 characters");
        checkArgument(attributeValidator.isNotNull(request.getEnvironment()), "environment must not be null");

        if (tenantRepository.findBySubdomain(request.getSubdomain()).isPresent()) {
            throw new DuplicateResourceException("duplicate subdomain: " + request.getSubdomain());
        }

        final var identityResponse = identityService.getIdentity(servletRequest);

        final var identity = identityRepository.findById(identityResponse.getId())
                .orElseThrow(() -> new ResourceNotFoundException("identity not found for id: " + identityResponse.getId()));

        final var tenant = tenantRepository.save(tenantInstantiator.instantiate(request));
        // todo: fix this ugliness
        loginDescriptorInstantiator.instantiate(tenant);

        tenant.getManagingIdentities().add(identity);
        identity.getManagedTenants().add(tenant);

        return responseMapper.map(tenant);
    }
}
