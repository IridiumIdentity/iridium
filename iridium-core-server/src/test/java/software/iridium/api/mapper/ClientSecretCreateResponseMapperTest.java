/*
 *  Copyright (C) Josh Fischer - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Josh Fischer <josh@joshfischer.io>, 2023.
 */
package software.iridium.api.mapper;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.equalTo;

import java.util.Date;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import software.iridium.api.entity.ClientSecretEntity;

class ClientSecretCreateResponseMapperTest {

  private ClientSecretCreateResponseMapper subject;

  @BeforeEach
  public void setupForEachTestCase() {
    subject = new ClientSecretCreateResponseMapper();
  }

  @Test
  public void map_AllGood_MapsAsExpected() {
    final var id = "the id";
    final var secretKey = "the secret key";
    final var created = new Date();
    final var entity = new ClientSecretEntity();
    entity.setId(id);
    entity.setCreated(created);
    entity.setSecretKey(secretKey);

    final var response = subject.map(entity, secretKey);

    MatcherAssert.assertThat(response.getId(), is(equalTo(id)));
    MatcherAssert.assertThat(response.getSecretKey(), is(equalTo(secretKey)));
    MatcherAssert.assertThat(response.getCreated(), is(equalTo(created)));
  }
}
