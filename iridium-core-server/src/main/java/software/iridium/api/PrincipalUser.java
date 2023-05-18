package software.iridium.api;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.security.Principal;
import java.util.List;

public class PrincipalUser extends User implements Principal {
  private static final long serialVersionUID = -3997242419392271736L;

  private String emailAddress;
  private String authToken;

  public PrincipalUser(
      final String authToken, final String emailAddress, final List<GrantedAuthority> authorities) {
    super(emailAddress, "password", authorities);
    this.authToken = authToken;
  }

  @Override
  public String getName() {
    return emailAddress;
  }

  public String getAuthToken() {
    return authToken;
  }
}
