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
package software.iridium.api.email.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EmailSendRequest implements Serializable {
  private static final long serialVersionUID = 6087314721700142044L;

  public static final String MEDIA_TYPE = "application/vnd.carbonid.email-send-request.1+json";

  private String to;

  private String subject;

  private Map<String, Object> properties = new HashMap<>();

  private List<EmailAttachment> attachments = new ArrayList<>();

  private String template;

  public static String getMediaType() {
    return MEDIA_TYPE;
  }

  public String getTo() {
    return to;
  }

  public void setTo(final String to) {
    this.to = to;
  }

  public String getSubject() {
    return subject;
  }

  public void setSubject(final String subject) {
    this.subject = subject;
  }

  public Map<String, Object> getProperties() {
    return properties;
  }

  public void setProperties(final Map<String, Object> properties) {
    this.properties = properties;
  }

  public List<EmailAttachment> getAttachments() {
    return attachments;
  }

  public void setAttachments(final List<EmailAttachment> attachments) {
    this.attachments = attachments;
  }

  public String getTemplate() {
    return template;
  }

  public void setTemplate(final String template) {
    this.template = template;
  }
}
