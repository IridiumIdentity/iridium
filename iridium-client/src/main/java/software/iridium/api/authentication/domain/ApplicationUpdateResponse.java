package software.iridium.api.authentication.domain;

import java.io.Serial;
import java.io.Serializable;

public class ApplicationUpdateResponse implements Serializable {
    public static final String MEDIA_TYPE =
            "application/vnd.iridium.id.application-update-response.1+json";
    @Serial
    private static final long serialVersionUID = -1487585095563737742L;

    private String id;

    private String name;

    private String applicationTypeId;

    private String homepageURL;

    private String tenantId;

    private String description;

    private String callbackURL;

    private String privacyPolicyUrl;

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getApplicationTypeId() {
        return applicationTypeId;
    }

    public void setApplicationTypeId(final String applicationTypeId) {
        this.applicationTypeId = applicationTypeId;
    }

    public String getHomepageURL() {
        return homepageURL;
    }

    public void setHomepageURL(final String homepageURL) {
        this.homepageURL = homepageURL;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public String getCallbackURL() {
        return callbackURL;
    }

    public void setCallbackURL(final String callbackURL) {
        this.callbackURL = callbackURL;
    }

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(final String tenantId) {
        this.tenantId = tenantId;
    }

    public String getPrivacyPolicyUrl() {
        return privacyPolicyUrl;
    }

    public void setPrivacyPolicyUrl(final String privacyPolicyUrl) {
        this.privacyPolicyUrl = privacyPolicyUrl;
    }
}
