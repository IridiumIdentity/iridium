/*
 *  Copyright (C) Josh Fischer - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Josh Fischer <josh@joshfischer.io>, 2023.
 */
package software.iridium.api.instantiator;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import software.iridium.api.authentication.domain.CreateIdentityRequest;
import software.iridium.api.authentication.domain.GithubProfileResponse;
import software.iridium.api.entity.ExternalIdentityProviderEntity;
import software.iridium.api.entity.IdentityEmailEntity;
import software.iridium.api.entity.IdentityEntity;

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
  public void instantiate_AllGood_InstantiatesAsExpected() {
    final var emailAddress = "you@nowhere.com";
    final var encodedPassword = "encodedPassword";
    final var request = new CreateIdentityRequest();
    request.setUsername(emailAddress);
    final var emailEntity = new IdentityEmailEntity();
    emailEntity.setPrimary(true);
    emailEntity.setEmailAddress(emailAddress);
    final var tenantId = "the tenantId";

    when(mockEmailInstantiator.instantiatePrimaryEmail(same(emailAddress))).thenReturn(emailEntity);

    final var response = subject.instantiate(request, encodedPassword, tenantId);

    verify(mockEmailInstantiator).instantiatePrimaryEmail(same(emailAddress));

    MatcherAssert.assertThat(response.getEncodedPassword(), is(equalTo(encodedPassword)));
    MatcherAssert.assertThat(response.getParentTenantId(), is(equalTo(tenantId)));
    MatcherAssert.assertThat(
        response.getPrimaryEmail().getEmailAddress(), is(equalTo(emailAddress)));
    final var email = response.getPrimaryEmail();
    MatcherAssert.assertThat(email.getIdentity(), sameInstance(response));
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

    subject.instantiateFromGithub(githubResponse, provider);

    verify(mockEmailInstantiator).instantiatePrimaryEmail(same(emailAddress));
    verify(mockPropertyInstantiator)
        .instantiateGithubProperties(same(githubResponse), any(IdentityEntity.class));
  }
}
