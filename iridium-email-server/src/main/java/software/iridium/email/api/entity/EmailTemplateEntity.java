package software.iridium.email.api.entity;

import software.iridium.api.entity.UuidIdentifiableAndAuditable;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@AttributeOverride(name="id", column=@Column(name="email_template_id"))
@Table(name="email_templates")
public class EmailTemplateEntity extends UuidIdentifiableAndAuditable {

    private static final long serialVersionUID = 7103996733194711078L;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "tenant_id", nullable = false, length = 36)
    private String tenantId;

    @Column(name = "file_path", nullable = false, length = 255)
    private String filePath;

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(final String tenantId) {
        this.tenantId = tenantId;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(final String filePath) {
        this.filePath = filePath;
    }
}
