/*
 *  Copyright (C) Josh Fischer - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Josh Fischer <josh@joshfischer.io>, 2022.
 */

package software.iridium.api.mapper;


import org.hamcrest.MatcherAssert;
import software.iridium.api.authentication.domain.Environment;
import software.iridium.api.entity.TenantEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

class CreateTenantResponseMapperTest {

    private CreateTenantResponseMapper subject;

    @BeforeEach
    public void setupForEachTestCase() {
        subject = new CreateTenantResponseMapper();
    }

    @Test
    public void map_AllGood_MapsAsExpected() {
        final var id = "the id";
        final var subdomain = "the subdomin";
        final var environment = Environment.DEVELOPMENT;
        final var entity = new TenantEntity();
        entity.setId(id);
        entity.setSubdomain(subdomain);
        entity.setEnvironment(environment);

        final var response = subject.map(entity);

        MatcherAssert.assertThat(response.getId(), is(equalTo(id)));
        MatcherAssert.assertThat(response.getEnvironment(), is(equalTo(environment)));
        MatcherAssert.assertThat(response.getSubdomain(), is(equalTo(subdomain)));
    }
}
