package com.moat.security;

import com.moat.entity.MOATUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SecMOATUserDetails implements UserDetails {
  private final static Logger logger =
      LoggerFactory.getLogger(SecMOATUserDetails.class);

  private final MOATUser user;

  public SecMOATUserDetails(MOATUser user) {
    logger.debug("Constructing SecMoatUserDetails.");

    this.user = user;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    logger.debug("In getAuthorities() in SecMOATUserDetails.");

    List<GrantedAuthority> roles = new ArrayList<>();

    String role = user.getRole();

    if (role.equals("USER")) {
      roles.add(new SimpleGrantedAuthority("ROLE_USER"));
    } else if (role.equals("ADMIN")) {
      roles.add(new SimpleGrantedAuthority("ROLE_USER"));
      roles.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
    } else {
      // TODO: Throw runtime exception?
      logger.error("Couldn't find user role!");
    }

    return roles;
  }

  @Override
  public String getPassword() {
    return user.getPassword();
  }

  @Override
  public String getUsername() {
    return user.getUsername();
  }

  // TODO: Think how to get this data.
  public Long getId() {
    return user.getId();
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return !user.isBanned();
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return user.isVerified();
  }
}
