package com.moat.security;

import com.moat.entity.MOATAdmin;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class SecMOATAdminDetails implements UserDetails {
  private final MOATAdmin admin;

  public SecMOATAdminDetails(MOATAdmin admin) {
    this.admin = admin;
  }

  // TODO: Fix
  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return List.<GrantedAuthority>of(new SimpleGrantedAuthority("ROLE_USER"),
        new SimpleGrantedAuthority("ROLE_ADMIN"));
  }

  @Override
  public String getPassword() {
    return admin.getPassword();
  }

  @Override
  public String getUsername() {
    return admin.getUsername();
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return admin.isVerified();
  }
}
