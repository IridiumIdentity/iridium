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
package software.iridium.api.instantiator;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import software.iridium.api.authentication.domain.GithubProfileResponse;
import software.iridium.api.authentication.domain.GoogleProfileResponse;
import software.iridium.entity.IdentityEntity;
import software.iridium.entity.IdentityPropertyEntity;

@ExtendWith(MockitoExtension.class)
class IdentityPropertyEntityInstantiatorTest {

  private IdentityPropertyEntityInstantiator subject;

  @BeforeEach
  public void setupForEachTestCase() {
    subject = new IdentityPropertyEntityInstantiator();
  }

  @Test
  public void instantiateGithubProperties_AllGood_InstantiatesAsExpected() {
    final var avatarUrl = "http://somelocation.com";
    final var login = "username123";
    final var name = "first last";
    final var email = "you@nowhere.com";
    final var externalId = "the exteranl id";
    final var githubResponse = new GithubProfileResponse();
    githubResponse.setAvatarUrl(avatarUrl);
    githubResponse.setLogin(login);
    githubResponse.setEmail(email);
    githubResponse.setName(name);
    final var identity = new IdentityEntity();

    subject.instantiateFromExternalProfile(githubResponse, identity);

    List<IdentityPropertyEntity> properties = identity.getIdentityProperties();
    assertThat(properties.size(), is(equalTo(4)));
    for (IdentityPropertyEntity property : properties) {
      switch (property.getName()) {
        case "avatarUrl" -> assertThat(property.getValue(), is(equalTo(avatarUrl)));
        case "login" -> assertThat(property.getValue(), is(equalTo(login)));
        case "name" -> assertThat(property.getValue(), is(equalTo(name)));
        case "email" -> assertThat(property.getValue(), is(equalTo(email)));
        default -> throw new RuntimeException(
            "Property doesn't match expected: " + property.getName());
      }
    }
  }

  @Test
  public void instantiateGoogleProperties_AllGood_InstantiatesAsExpected() {
    final var familyName = "the family name";
    final var name = "name";
    final var picture = "the picture url";
    final var locale = "the locale";
    final var email = "the email";
    final var givenName = "the given name";
    final var hd = "hd";
    final var verifiedEmail = true;
    final var googleProfileResponse = new GoogleProfileResponse();
    googleProfileResponse.setFamilyName(familyName);
    googleProfileResponse.setName(name);
    googleProfileResponse.setPicture(picture);
    googleProfileResponse.setLocale(locale);
    googleProfileResponse.setEmail(email);
    googleProfileResponse.setGivenName(givenName);
    googleProfileResponse.setHd(hd);
    googleProfileResponse.setVerifiedEmail(verifiedEmail);
    final var identity = new IdentityEntity();

    subject.instantiateFromExternalProfile(googleProfileResponse, identity);

    List<IdentityPropertyEntity> properties = identity.getIdentityProperties();
    assertThat(properties.size(), is(equalTo(6)));
    for (IdentityPropertyEntity property : properties) {
      switch (property.getName()) {
        case "familyName" -> assertThat(property.getValue(), is(equalTo(familyName)));
        case "name" -> assertThat(property.getValue(), is(equalTo(name)));
        case "picture" -> assertThat(property.getValue(), is(equalTo(picture)));
        case "givenName" -> assertThat(property.getValue(), is(equalTo(givenName)));
        case "hd" -> assertThat(property.getValue(), is(equalTo(hd)));
        case "verifiedEmail" -> assertThat(property.getValue(), is(equalTo("true")));
        default -> throw new RuntimeException(
            "Property doesn't match expected: " + property.getName());
      }
    }
  }
}
