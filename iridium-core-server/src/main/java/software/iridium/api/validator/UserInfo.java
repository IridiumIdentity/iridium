package software.iridium.api.validator;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import software.iridium.api.entity.IdentityEntity;

import java.io.Serial;
import java.util.ArrayList;
import java.util.Collection;

public class UserInfo extends User {

  @Serial
  private static final long serialVersionUID = -1300015092363456591L;

  public UserInfo(IdentityEntity identity) {
    super(String.valueOf(identity.getId()), "", new ArrayList<>());
  }

  public UserInfo(IdentityEntity identity, Collection<? extends GrantedAuthority> grantedAuthorities) {
    super(String.valueOf(identity.getId()), "", grantedAuthorities);
  }
}
