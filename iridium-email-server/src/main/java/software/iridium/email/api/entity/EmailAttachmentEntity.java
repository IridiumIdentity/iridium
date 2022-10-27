package software.iridium.email.api.entity;

import software.iridium.api.entity.UuidIdentifiableAndAuditable;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@AttributeOverride(name="id", column=@Column(name="email_attachment_id"))
@Table(name="email_attachment")
public class EmailAttachmentEntity extends UuidIdentifiableAndAuditable {

    private static final long serialVersionUID = 5881323464921939514L;

    @Column(name = "file_path", length = 255, nullable = false)
    private String filePath;

    @Column(name = "attachment_name", length = 100, nullable = false)
    private String attachmentName;

    @Column(name = "tenant_id", length = 36, nullable = false)
    private String tenantId;

    @ManyToOne
    @JoinColumn(name = "email_entity_id")
    private EmailSendEntity email;

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(final String filePath) {
        this.filePath = filePath;
    }

    public String getAttachmentName() {
        return attachmentName;
    }

    public void setAttachmentName(final String attachmentName) {
        this.attachmentName = attachmentName;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(final String tenantId) {
        this.tenantId = tenantId;
    }

    public EmailSendEntity getEmail() {
        return email;
    }

    public void setEmail(final EmailSendEntity email) {
        this.email = email;
    }
}
