package software.iridium.api.email.domain;

import java.io.Serializable;

public class EmailAttachment implements Serializable {

    private static final long serialVersionUID = -6177710152362088025L;
    private String attachmentFileName;

    private String fileId;

    public String getAttachmentFileName() {
        return attachmentFileName;
    }

    public void setAttachmentFileName(final String attachmentFileName) {
        this.attachmentFileName = attachmentFileName;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(final String fileId) {
        this.fileId = fileId;
    }
}
