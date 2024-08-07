package com.moat.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.moat.validator.group.PatchUserDetailsGroup;
import com.moat.validator.group.PatchUserPasswordGroup;
import com.moat.validator.misc.IdValid;
import com.moat.validator.misc.EmailValid;
import com.moat.validator.misc.PasswordValid;
import com.moat.validator.misc.UsernameValid;
import com.moat.validator.group.SaveUserGroup;

import javax.validation.groups.Default;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDTO {
  @IdValid(groups = {PatchUserDetailsGroup.class, PatchUserPasswordGroup.class,
      Default.class})
  private Long id;

  @UsernameValid(groups = {SaveUserGroup.class, PatchUserDetailsGroup.class,
      Default.class})
  private String username;

  @EmailValid(groups = {SaveUserGroup.class, PatchUserDetailsGroup.class,
      Default.class})
  private String email;

  @PasswordValid(groups = {SaveUserGroup.class, PatchUserPasswordGroup.class,
      Default.class})
  private String password;

  private Boolean banned;

  private Boolean verified;

  private String role;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public Boolean isBanned() {
    return banned;
  }

  public void setBanned(Boolean banned) {
    this.banned = banned;
  }

  public boolean isVerified() {
    return verified;
  }

  public void setVerified(Boolean verified) {
    this.verified = verified;
  }

  public String getRole() {
    return role;
  }

  public void setRole(String role) {
    this.role = role;
  }
}
