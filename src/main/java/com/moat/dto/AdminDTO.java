package com.moat.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.moat.validator.group.SaveAdminGroup;
import com.moat.validator.misc.EmailValid;
import com.moat.validator.misc.IdValid;
import com.moat.validator.misc.PasswordValid;
import com.moat.validator.misc.UsernameValid;

import javax.validation.groups.Default;

// TODO: Add validation
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AdminDTO {
  @IdValid
  private Long id;

  @UsernameValid(groups = {SaveAdminGroup.class, Default.class})
  private String username;

  @PasswordValid(groups = {SaveAdminGroup.class, Default.class})
  private String password;

  @EmailValid(groups = {SaveAdminGroup.class, Default.class})
  private String email;

  public AdminDTO() {}

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
}
