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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import software.iridium.api.authentication.domain.GithubProfileResponse;
import software.iridium.entity.ExternalIdentityProviderEntity;
import software.iridium.entity.IdentityEmailEntity;
import software.iridium.entity.IdentityEntity;

@ExtendWith(MockitoExtension.class)
class IdentityEntityInstantiatorTest {

  @Mock private EmailEntityInstantiator mockEmailInstantiator;
  @Mock private IdentityPropertyEntityInstantiator mockPropertyInstantiator;
  @InjectMocks private IdentityEntityInstantiator subject;

  @AfterEach
  public void ensureNoUnexpectedMockInteractions() {
    Mockito.verifyNoMoreInteractions(mockEmailInstantiator);
  }

  @Test
  public void instantiateFromGithub_AllGood_InstantiatesAsExpected() {
    final var githubResponse = new GithubProfileResponse();
    final var provider = new ExternalIdentityProviderEntity();
    final var emailAddress = "you@nowhere.com";
    githubResponse.setEmail(emailAddress);
    final var emailEntity = new IdentityEmailEntity();
    emailEntity.setPrimary(true);

    when(mockEmailInstantiator.instantiatePrimaryEmail(same(emailAddress))).thenReturn(emailEntity);

    subject.instantiate(githubResponse, provider);

    verify(mockEmailInstantiator).instantiatePrimaryEmail(same(emailAddress));
    verify(mockPropertyInstantiator)
        .instantiateFromExternalProfile(same(githubResponse), any(IdentityEntity.class));
  }
}
