package com.moat.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

// TODO: Add validation
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AdminDTO {
  private Long id;
  private String username;
  private String password;
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
