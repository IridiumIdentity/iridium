/*
 *  Copyright (C) Josh Fischer - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Josh Fischer <josh@joshfischer.io>, 2022.
 */

package software.iridium.api.mapper;

import org.hamcrest.MatcherAssert;
import software.iridium.api.entity.ProfileEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

class ProfileResponseMapperTest {

    private ProfileResponseMapper subject;

    @BeforeEach
    public void setUpForEachTestCase() {
        subject = new ProfileResponseMapper();
    }

    @Test
    public void map_AllGood_MapsAsExpected() {
        final var firstName = "the first name";
        final var lastName = "the last name";
        final var entity = new ProfileEntity();
        entity.setFirstName(firstName);
        entity.setLastName(lastName);

        final var response = subject.map(entity);

        MatcherAssert.assertThat(response.getFirstName(), is(equalTo(firstName)));
        MatcherAssert.assertThat(response.getLastName(), is(equalTo(lastName)));
    }

    @Test
    public void map_ProfileIsNull_ReturnsNull() {
        final var response = subject.map(null);

        assertThat(response, nullValue());
    }
}
