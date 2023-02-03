/*
 *  Copyright (C) Josh Fischer - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Josh Fischer <josh@joshfischer.io>, 2023.
 */
package software.iridium.api.mapper;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.ArrayList;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import software.iridium.api.entity.ApplicationEntity;

class ApplicationSummaryMapperTest {

  private ApplicationSummaryMapper subject;

  @BeforeEach
  public void setupForEachTestCase() {
    subject = new ApplicationSummaryMapper();
  }

  @Test
  public void mapToSummaries_AllGood_MapsAsExpected() {
    final var id = "the id";
    final var name = "the name";
    final var iconUrl = "the icon url";
    final var entity = new ApplicationEntity();
    entity.setId(id);
    entity.setName(name);
    entity.setIconUrl(iconUrl);
    final var list = new ArrayList<ApplicationEntity>();
    list.add(entity);

    final var responses = subject.mapToSummaries(list);

    assertThat(responses.size(), is(equalTo(1)));
    final var response = responses.get(0);
    MatcherAssert.assertThat(response.getId(), is(equalTo(id)));
    MatcherAssert.assertThat(response.getName(), is(equalTo(name)));
    MatcherAssert.assertThat(response.getIconUrl(), is(equalTo(iconUrl)));
  }

  @Test
  public void mapToList_EmptyList_MapsAsExpected() {
    final var list = new ArrayList<ApplicationEntity>();

    final var response = subject.mapToSummaries(list);

    assertThat(response.isEmpty(), is(equalTo(true)));
  }
}
