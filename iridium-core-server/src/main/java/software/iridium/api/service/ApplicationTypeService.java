/*
 *  Copyright (C) Josh Fischer - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Josh Fischer <josh@joshfischer.io>, 2022.
 */

package software.iridium.api.service;

import software.iridium.api.repository.ApplicationTypeEntityRepository;
import software.iridium.api.authentication.domain.ApplicationTypeSummary;
import software.iridium.api.mapper.ApplicationTypeSummaryMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ApplicationTypeService {

    @Resource
    private ApplicationTypeEntityRepository applicationTypeRepository;
    @Resource
    private ApplicationTypeSummaryMapper summaryMapper;

    @Transactional(propagation = Propagation.SUPPORTS)
    public List<ApplicationTypeSummary> getAll() {
        return summaryMapper.mapToList(applicationTypeRepository.findAll());
    }
}
