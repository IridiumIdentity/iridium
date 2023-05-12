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
package software.iridium.api.authentication.domain;

import java.io.Serializable;

public class ApplicationCreateRequest implements Serializable {

  private static final long serialVersionUID = -6208748801543357305L;

  public static final String MEDIA_TYPE =
      "application/vnd.iridium.id.application-create-request.1+json";

  private String name;

  private String applicationTypeId;

  private String homepageURL;

  private String description;

  private String callbackURL;

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
}
