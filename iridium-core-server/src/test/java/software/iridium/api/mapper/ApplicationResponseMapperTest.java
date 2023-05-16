package software.iridium.api.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import software.iridium.api.entity.ApplicationEntity;
import software.iridium.api.entity.ApplicationTypeEntity;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

class ApplicationResponseMapperTest {

    private ApplicationResponseMapper subject;

    @BeforeEach
    public void setUpForEachTestCase() {
        subject = new ApplicationResponseMapper();
    }

    @Test
    public void map_AllGood_MapsAsExpected() {
        final var applicationId = "the app id";
        final var name = "the app name";
        final var orgId = "the org id";
        final var appTypeId = "the app type id";
        final var description = "the description";
        final var callbackUrl = "http://somewhere.com/callback";
        final var homepageUrl = "http://somewhere.com";
        final var privacyPolicyUrl = "http://privacy.com";

        final var applicationType = new ApplicationTypeEntity();
        applicationType.setId(appTypeId);
        final var entity = new ApplicationEntity();
        entity.setName(name);
        entity.setTenantId(orgId);
        entity.setApplicationType(applicationType);
        entity.setId(applicationId);
        entity.setPrivacyPolicyUrl(privacyPolicyUrl);
        entity.setDescription(description);
        entity.setRedirectUri(callbackUrl);
        entity.setHomePageUrl(homepageUrl);

        final var response = subject.map(entity);

        assertThat(response.getName(), is(equalTo(name)));
        assertThat(response.getApplicationTypeId(), is(equalTo(appTypeId)));
        assertThat(response.getTenantId(), is(equalTo(orgId)));
        assertThat(response.getId(), is(equalTo(applicationId)));
        assertThat(response.getDescription(), is(equalTo(description)));
        assertThat(response.getCallbackURL(), is(equalTo(callbackUrl)));
        assertThat(response.getHomepageURL(), is(equalTo(homepageUrl)));
        assertThat(response.getPrivacyPolicyUrl(), is(equalTo(privacyPolicyUrl)));
    }

}
