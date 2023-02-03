/*
 *  Copyright (C) Josh Fischer - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Josh Fischer <josh@joshfischer.io>, 2023.
 */
package software.iridium.api.mapper;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import software.iridium.api.authentication.domain.ExternalProviderLoginDescriptorResponse;
import software.iridium.api.entity.ExternalIdentityProviderEntity;
import software.iridium.api.entity.LoginDescriptorEntity;
import software.iridium.api.entity.TenantEntity;

@ExtendWith(MockitoExtension.class)
class LoginDescriptorResponseMapperTest {

  @Mock private ExternalProviderLoginDescriptorResponseMapper mockResponseMapper;
  @InjectMocks private LoginDescriptorResponseMapper subject;

  @Test
  public void map_AllGood_MapsAsExpected() {
    final var allowGoogle = true;
    final var allowGithub = false;
    final var allowMicrosoft = true;
    final var allowApple = false;
    final var iconPath = "the path";
    final var pageTitle = "the page title";
    final var tenantName = "the tenant";
    final var usernameLabel = "the username label";
    final var usernameErrorHint = "the username error hint";
    final var usernameType = "the type";
    final var usernamePlaceholder = "the placeholder";
    final var entity = new LoginDescriptorEntity();
    entity.setAllowGithub(allowGithub);
    entity.setAllowGoogle(allowGoogle);
    entity.setAllowMicrosoft(allowMicrosoft);
    entity.setAllowApple(allowApple);
    entity.setIconPath(iconPath);
    entity.setPageTitle(pageTitle);
    entity.setDisplayName(tenantName);
    entity.setUsernameLabel(usernameLabel);
    entity.setUsernameErrorHint(usernameErrorHint);
    entity.setUsernameType(usernameType);
    entity.setUsernamePlaceholder(usernamePlaceholder);
    final var tenant = new TenantEntity();
    entity.setTenant(tenant);
    final var externalProvider = new ExternalIdentityProviderEntity();
    final var externalProviders = new ArrayList<ExternalIdentityProviderEntity>();
    final var descriptorResponses = new ArrayList<ExternalProviderLoginDescriptorResponse>();
    tenant.setExternalIdentityProviders(externalProviders);

    when(mockResponseMapper.mapList(same(externalProviders))).thenReturn(descriptorResponses);

    final var response = subject.map(entity);

    verify(mockResponseMapper).mapList(same(externalProviders));

    MatcherAssert.assertThat(response.getIconPath(), is(equalTo(iconPath)));
    MatcherAssert.assertThat(response.getPageTitle(), is(equalTo(pageTitle)));
    MatcherAssert.assertThat(response.getDisplayName(), is(equalTo(tenantName)));
    MatcherAssert.assertThat(response.getUsernameErrorHint(), is(equalTo(usernameErrorHint)));
    MatcherAssert.assertThat(response.getUsernameLabel(), is(equalTo(usernameLabel)));
    MatcherAssert.assertThat(response.getUsernamePlaceholder(), is(equalTo(usernamePlaceholder)));
    MatcherAssert.assertThat(response.getUsernameType(), is(equalTo(usernameType)));
    MatcherAssert.assertThat(
        response.getExternalProviderDescriptors(), sameInstance(descriptorResponses));
  }
}
