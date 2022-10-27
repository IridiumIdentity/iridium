/*
 *  Copyright (C) Josh Fischer - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Josh Fischer <josh@joshfischer.io>, 2022.
 */

package software.iridium.api.mapper;


import org.hamcrest.MatcherAssert;
import software.iridium.api.entity.ApplicationTypeEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

class ApplicationTypeSummaryMapperTest {

    private ApplicationTypeSummaryMapper subject;

    @BeforeEach
    public void setUpForEachTestCase() {
        subject = new ApplicationTypeSummaryMapper();
    }

    @Test
    public void mapToList_AllGood_MapsAsExpected() {
        final var id = "the id";
        final var name = "the name";
        final var entity = new ApplicationTypeEntity();
        entity.setId(id);
        entity.setName(name);
        final var list = new ArrayList<ApplicationTypeEntity>();
        list.add(entity);

        final var responses = subject.mapToList(list);

        assertThat(responses.size(), is(equalTo(1)));
        final var response = responses.get(0);
        MatcherAssert.assertThat(response.getId(), is(equalTo(id)));
        MatcherAssert.assertThat(response.getName(), is(equalTo(name)));
    }

    @Test
    public void mapToList_EmptyList_MapsAsExpected() {
        final var list = new ArrayList<ApplicationTypeEntity>();

        final var response = subject.mapToList(list);

        assertThat(response.isEmpty(), is(equalTo(true)));
    }
}
