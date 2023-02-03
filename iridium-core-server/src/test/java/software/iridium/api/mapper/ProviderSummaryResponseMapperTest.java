/*
 *  Copyright (C) Josh Fischer - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Josh Fischer <josh@joshfischer.io>, 2023.
 */
package software.iridium.api.mapper;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import java.util.Arrays;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import software.iridium.api.entity.ExternalIdentityProviderTemplateEntity;

class ProviderSummaryResponseMapperTest {

  private ProviderSummaryResponseMapper subject;

  @BeforeEach
  public void setUpForEachTestCase() {
    subject = new ProviderSummaryResponseMapper();
  }

  @Test
  public void mapList_AllGood_BehavesAsExpected() {
    final var entityId = "the id";
    final var entityName = "the name";
    final var iconPath = "theIconPath";
    final var entity = new ExternalIdentityProviderTemplateEntity();
    entity.setId(entityId);
    entity.setName(entityName);
    entity.setIconPath(iconPath);

    final var responses = subject.mapList(Arrays.asList(entity));

    assertThat(responses.size(), is(equalTo(1)));
    final var response = responses.get(0);
    MatcherAssert.assertThat(response.getId(), is(equalTo(entityId)));
    MatcherAssert.assertThat(response.getName(), is(equalTo(entityName)));
    MatcherAssert.assertThat(response.getIconPath(), is(equalTo(iconPath)));
  }

  @Test
  public void mapList_EmtpyList_ReturnsAsExpected() {

    final var response = subject.mapList(Arrays.asList());

    assertThat(response.size(), is(equalTo(0)));
  }
}
