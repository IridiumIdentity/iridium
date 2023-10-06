/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package software.iridium.tracker.mapper;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import software.iridium.api.tracker.AggregateProviderSummary;
import software.iridium.entity.ExternalIdentityProviderEntity;
import software.iridium.entity.IdentityEntity;

class ProviderCountResponseMapperTest {

  private ProviderCountResponseMapper subject;

  @BeforeEach
  public void setUpForEachTestCase() {
    subject = new ProviderCountResponseMapper();
  }

  @Test
  public void map_AllGood_MapsAsExpected() {
    final var id = "the id";
    final var providerName = "GitHub";
    final var provider = new ExternalIdentityProviderEntity();
    provider.setDisplayName(providerName);
    final var entity = new IdentityEntity();
    entity.setId(id);
    entity.setProvider(provider);
    final var list = new ArrayList<IdentityEntity>();
    list.add(entity);

    final var responses = subject.map(list);

    assertThat(responses.size(), is(equalTo(1)));
    final var response = responses.get(0);
    assertThat(response.getName(), is(equalTo("GitHub")));
    assertThat(response.getValue(), is(equalTo(1)));
  }

  @Test
  public void map_MultipleGitHubAccounts_MapsAsExpected() {
    final var providerName = "GitHub";
    final var github = new ExternalIdentityProviderEntity();
    github.setDisplayName(providerName);
    final var identityOne = new IdentityEntity();
    identityOne.setProvider(github);

    final var identityTwo = new IdentityEntity();
    identityTwo.setProvider(github);

    final var identityThree = new IdentityEntity();
    identityThree.setProvider(github);
    final var list = new ArrayList<IdentityEntity>();
    list.add(identityOne);
    list.add(identityTwo);
    list.add(identityThree);

    final var responses = subject.map(list);

    assertThat(responses.size(), is(equalTo(1)));
    final var response = responses.get(0);
    assertThat(response.getName(), is(equalTo("GitHub")));
    assertThat(response.getValue(), is(equalTo(3)));
  }

  @Test
  public void map_MultipleProvidersAccounts_MapsAsExpected() {
    final var gitHubName = "GitHub";
    final var github = new ExternalIdentityProviderEntity();
    final var googleName = "Google";
    final var google = new ExternalIdentityProviderEntity();
    github.setDisplayName(gitHubName);
    google.setDisplayName(googleName);
    final var identityOne = new IdentityEntity();
    identityOne.setProvider(github);

    final var identityTwo = new IdentityEntity();
    identityTwo.setProvider(google);

    final var identityThree = new IdentityEntity();
    identityThree.setProvider(github);
    final var list = new ArrayList<IdentityEntity>();
    list.add(identityOne);
    list.add(identityTwo);
    list.add(identityThree);

    final var responses = subject.map(list);

    assertThat(responses.size(), is(equalTo(2)));
    final var response = responses.get(0);
    for (AggregateProviderSummary summary : responses) {
      if (response.getName().equals("GitHub")) {
        assertThat(response.getValue(), is(equalTo(2)));
      } else if (response.getName().equals("Google")) {
        assertThat(response.getValue(), is(equalTo(1)));
      } else {
        throw new RuntimeException("Unexpected provider found");
      }
    }
  }

  @Test
  public void map_EmptyList_MapsAsExpected() {
    final var list = new ArrayList<IdentityEntity>();

    final var response = subject.map(list);

    assertThat(response.isEmpty(), is(equalTo(true)));
  }
}
