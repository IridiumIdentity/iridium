/*
 *  Copyright (C) Josh Fischer - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Josh Fischer <josh@joshfischer.io>, 2023.
 */
package software.iridium.api.mapper;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import software.iridium.api.entity.IdentityEmailEntity;
import software.iridium.api.entity.IdentityEntity;
import software.iridium.api.entity.RoleEntity;

class IdentityEntityMapperTest {

  private IdentityEntityMapper subject;

  @BeforeEach
  public void setUpForEachTestCase() {
    subject = new IdentityEntityMapper();
  }

  @Test
  public void map_AllGood_MapsAsExpected() {
    final var id = "someId";
    final var emailAddress = "someUserName@nowhere.com";
    final var roleName = "roleName";
    final var role = new RoleEntity();
    role.setName(roleName);
    final var entity = new IdentityEntity();
    final var email = new IdentityEmailEntity();
    email.setEmailAddress(emailAddress);
    email.setPrimary(true);
    entity.setId(id);
    entity.getEmails().add(email);
    entity.getRoles().add(role);

    final var response = subject.map(entity);

    MatcherAssert.assertThat(response.getId(), is(equalTo(id)));
    MatcherAssert.assertThat(response.getUsername(), is(equalTo(emailAddress)));
    MatcherAssert.assertThat(response.getRoles().size(), is(equalTo(1)));
    for (String domainRoleName : response.getRoles()) {
      assertThat(domainRoleName, is(equalTo(roleName)));
    }
  }

  @Test
  public void map_EntityIsNull_ReturnsNull() {

    final var response = subject.map(null);

    assertThat(response, nullValue());
  }
}
