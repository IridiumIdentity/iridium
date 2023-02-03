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
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import software.iridium.api.authentication.domain.TenantSummary;
import software.iridium.api.entity.TenantEntity;

class TenantSummaryMapperTest {

  private TenantSummaryMapper subject;

  @BeforeEach
  public void setupForEachTestCase() {
    subject = new TenantSummaryMapper();
  }

  @Test
  public void mapToList_AllGood_EmptyList() {
    final var entities = new ArrayList<TenantEntity>();

    final var responses = subject.mapToList(entities);

    assertTrue(responses.isEmpty());
  }

  @Test
  public void mapToList_AllGood_BehavesAsExpected() {
    final var tenantOneId = "the one id";
    final var tenantOneSubdomain = "the one name";
    final var tenantTwoId = "the two id";
    final var tenantTwoSubdomain = "the two name";
    final var tenantOne = new TenantEntity();
    tenantOne.setId(tenantOneId);
    tenantOne.setSubdomain(tenantOneSubdomain);
    final var tenantTwo = new TenantEntity();
    tenantTwo.setId(tenantTwoId);
    tenantTwo.setSubdomain(tenantTwoSubdomain);

    final var entities = Arrays.asList(tenantOne, tenantTwo);

    final var summaries = subject.mapToList(entities);

    assertThat(summaries.size(), is(equalTo(entities.size())));

    for (TenantSummary summary : summaries) {
      if (summary.getId().equals(tenantOneId)) {
        assertThat(summary.getSubdomain(), is(equalTo(tenantOneSubdomain)));
      } else if (summary.getId().equals(tenantTwoId)) {
        assertThat(summary.getSubdomain(), is(equalTo(tenantTwoSubdomain)));
      } else {
        throw new RuntimeException("mismatching tenant ids");
      }
    }
  }
}
