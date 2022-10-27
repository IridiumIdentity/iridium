/*
 *  Copyright (C) Josh Fischer - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Josh Fischer <josh@joshfischer.io>, 2022.
 */

package software.iridium.api.service;

import software.iridium.api.authentication.domain.ApplicationTypeSummary;
import software.iridium.api.entity.ApplicationTypeEntity;
import software.iridium.api.mapper.ApplicationTypeSummaryMapper;
import software.iridium.api.repository.ApplicationTypeEntityRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ApplicationTypeServiceTest {

    @Mock
    private ApplicationTypeEntityRepository mockApplicationTypeRepository;
    @Mock
    private ApplicationTypeSummaryMapper mockSummaryMapper;
    @InjectMocks
    private ApplicationTypeService subject;

    @Test
    public void getAll_AllGood_BehavesAsExpected() {
        List<ApplicationTypeEntity> entities = new ArrayList<>();
        List<ApplicationTypeSummary> summaries = new ArrayList<>();

        when(mockApplicationTypeRepository.findAll()).thenReturn(entities);
        when(mockSummaryMapper.mapToList(same(entities))).thenReturn(summaries);

        subject.getAll();

        verify(mockApplicationTypeRepository).findAll();
        verify(mockSummaryMapper).mapToList(same(entities));
    }
}
