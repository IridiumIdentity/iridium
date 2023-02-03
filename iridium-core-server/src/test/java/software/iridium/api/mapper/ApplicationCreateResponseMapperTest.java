/*
 *  Copyright (C) Josh Fischer - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Josh Fischer <josh@joshfischer.io>, 2023.
 */
package software.iridium.api.mapper;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;

import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import software.iridium.api.entity.ApplicationEntity;
import software.iridium.api.entity.ApplicationTypeEntity;

class ApplicationCreateResponseMapperTest {

  private ApplicationResponseMapper subject;

  @BeforeEach
  public void setupForEachTestCase() {
    subject = new ApplicationResponseMapper();
  }

  @Test
  public void map_AllGood_MapsAsExpected() {
    final var applicationId = "the app id";
    final var name = "the app name";
    final var orgId = "the org id";
    final var appTypeId = "the app type id";
    final var applicationType = new ApplicationTypeEntity();
    applicationType.setId(appTypeId);
    final var entity = new ApplicationEntity();
    entity.setName(name);
    entity.setTenantId(orgId);
    entity.setApplicationType(applicationType);
    entity.setId(applicationId);

    final var response = subject.map(entity);

    MatcherAssert.assertThat(response.getName(), is(equalTo(name)));
    MatcherAssert.assertThat(response.getApplicationTypeId(), is(equalTo(appTypeId)));
    MatcherAssert.assertThat(response.getTenantId(), is(equalTo(orgId)));
    MatcherAssert.assertThat(response.getId(), is(equalTo(applicationId)));
  }
}
