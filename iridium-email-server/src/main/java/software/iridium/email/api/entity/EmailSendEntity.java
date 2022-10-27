package software.iridium.email.api.entity;

import software.iridium.api.entity.UuidIdentifiableAndAuditable;

import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Entity
@AttributeOverride(name="id", column=@Column(name="email_send_id"))
@Table(name="email_sends")
public class EmailSendEntity extends UuidIdentifiableAndAuditable {

    private static final long serialVersionUID = 5369868319213866813L;

    @Column(name = "tenant_id", nullable = false, length = 36)
    private String tenantId;

    @Column(name = "email_template_id", nullable = false, length = 36)
    private String emailTemplateId;

    @Column(name = "body", nullable = false, length = 512)
    private String body;

    @Column(name = "to", nullable = false, length = 100)
    private String to;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "email",fetch = FetchType.LAZY)
    private List<EmailAttachmentEntity> attachments = new ArrayList<>();

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(final String tenantId) {
        this.tenantId = tenantId;
    }

    public String getEmailTemplateId() {
        return emailTemplateId;
    }

    public void setEmailTemplateId(final String emailTemplateId) {
        this.emailTemplateId = emailTemplateId;
    }

    public String getBody() {
        return body;
    }

    public void setBody(final String body) {
        this.body = body;
    }

    public String getTo() {
        return to;
    }

    public void setTo(final String to) {
        this.to = to;
    }

    public List<EmailAttachmentEntity> getAttachments() {
        return attachments;
    }

    public void setAttachments(final List<EmailAttachmentEntity> attachments) {
        this.attachments = attachments;
    }
}
