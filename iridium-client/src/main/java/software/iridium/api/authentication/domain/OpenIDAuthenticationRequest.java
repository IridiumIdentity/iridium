/*
 *  Copyright (C) Josh Fischer - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Josh Fischer <josh@joshfischer.io>, 2023.
 */
package software.iridium.api.authentication.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;

public class OpenIDAuthenticationRequest implements Serializable {

  private static final long serialVersionUID = -2241796820066775989L;
  // required
  private String scope;

  // required
  @JsonProperty("response_type")
  private String response_type;

  // required
  @JsonProperty("client_id")
  private String client_id;

  // required
  @JsonProperty("redirect_uri")
  private String redirect_uri;

  // required
  @JsonProperty("state")
  private String state;

  @JsonProperty("code_challenge")
  private String code_challenge;

  @JsonProperty("code_challenge_method")
  private String code_challenge_method;

  public String getScope() {
    return scope;
  }

  public void setScope(final String scope) {
    this.scope = scope;
  }

  public String getResponse_type() {
    return response_type;
  }

  public void setResponse_type(final String response_type) {
    this.response_type = response_type;
  }

  public String getClient_id() {
    return client_id;
  }

  public void setClient_id(final String client_id) {
    this.client_id = client_id;
  }

  public String getRedirect_uri() {
    return redirect_uri;
  }

  public void setRedirect_uri(final String redirect_uri) {
    this.redirect_uri = redirect_uri;
  }

  public String getState() {
    return state;
  }

  public void setState(final String state) {
    this.state = state;
  }

  public String getCode_challenge() {
    return code_challenge;
  }

  public void setCode_challenge(final String code_challenge) {
    this.code_challenge = code_challenge;
  }

  public String getCode_challenge_method() {
    return code_challenge_method;
  }

  public void setCode_challenge_method(final String code_challenge_method) {
    this.code_challenge_method = code_challenge_method;
  }
}
