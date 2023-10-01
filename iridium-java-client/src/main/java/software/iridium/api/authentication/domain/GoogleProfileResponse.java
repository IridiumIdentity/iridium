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

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.HashMap;
import java.util.Map;

public class GoogleProfileResponse implements ExternalProviderProfile {

  @JsonProperty("family_name")
  private String familyName;

  private String name;

  private String picture;

  private String locale;

  private String email;

  @JsonProperty("given_name")
  private String givenName;

  private String id;

  @JsonProperty("verified_email")
  private Boolean verifiedEmail;

  private String hd;

  public String getFamilyName() {
    return familyName;
  }

  public void setFamilyName(final String familyName) {
    this.familyName = familyName;
  }

  public String getName() {
    return name;
  }

  public void setName(final String name) {
    this.name = name;
  }

  public String getPicture() {
    return picture;
  }

  public void setPicture(final String picture) {
    this.picture = picture;
  }

  public String getLocale() {
    return locale;
  }

  public void setLocale(final String locale) {
    this.locale = locale;
  }

  public void setEmail(final String email) {
    this.email = email;
  }

  public String getGivenName() {
    return givenName;
  }

  public void setGivenName(final String givenName) {
    this.givenName = givenName;
  }

  public String getId() {
    return id;
  }

  public void setId(final String id) {
    this.id = id;
  }

  public String getHd() {
    return hd;
  }

  public void setHd(final String hd) {
    this.hd = hd;
  }

  public Boolean getVerifiedEmail() {
    return verifiedEmail;
  }

  public void setVerifiedEmail(final Boolean verifiedEmail) {
    this.verifiedEmail = verifiedEmail;
  }

  @Override
  public Map<String, String> getProfileAttributes() {
    Map<String, String> attributes = new HashMap<>();
    attributes.put("familyName", familyName);
    attributes.put("name", name);
    attributes.put("picture", picture);
    attributes.put("givenName", givenName);
    attributes.put("hd", hd);
    attributes.put("verifiedEmail", String.valueOf(verifiedEmail));
    return attributes;
  }

  @Override
  public String getEmail() {
    return email;
  }

  @Override
  public String getExternalId() {
    return id;
  }
}
