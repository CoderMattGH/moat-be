package com.moat.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.moat.validator.misc.IdValid;
import com.moat.validator.user.EmailValid;
import com.moat.validator.user.PasswordValid;
import com.moat.validator.user.UsernameValid;
import com.moat.validator.user.group.SaveUserGroup;

import javax.validation.constraints.Null;
import javax.validation.groups.Default;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDTO {
  @IdValid
  @Null(groups = SaveUserGroup.class, message = "Id must be null!")
  private Long id;

  @UsernameValid(groups = {SaveUserGroup.class, Default.class})
  private String username;

  @EmailValid(groups = {SaveUserGroup.class, Default.class})
  private String email;

  @PasswordValid(groups = {SaveUserGroup.class, Default.class})
  private String password;

  private boolean banned;

  private boolean verified;

  public UserDTO() {}

  public UserDTO(Long id, String username, String email) {
    this.id = id;
    this.username = username;
    this.email = email;
  }

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

  public boolean isBanned() {
    return banned;
  }

  public void setBanned(boolean banned) {
    this.banned = banned;
  }

  public boolean isVerified() {
    return verified;
  }

  public void setVerified(boolean verified) {
    this.verified = verified;
  }
}
