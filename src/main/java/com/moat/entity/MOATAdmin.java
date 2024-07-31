package com.moat.entity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "moat_admin")
public class MOATAdmin {
  private final static Logger logger = LoggerFactory.getLogger(MOATAdmin.class);

  public final static int USERNAME_MIN_LENGTH = MOATUser.USERNAME_MIN_LENGTH;
  public final static int USERNAME_MAX_LENGTH = MOATUser.USERNAME_MIN_LENGTH;
  public final static String USERNAME_PATTERN = "^[A-Za-z0-9]+$";

  public final static int PASSWORD_MIN_LENGTH = MOATUser.PASSWORD_MIN_LENGTH;
  public final static int PASSWORD_MAX_LENGTH = MOATUser.PASSWORD_MAX_LENGTH;
  public final static String PASSWORD_PATTERN = MOATUser.PASSWORD_PATTERN;

  public final static int EMAIL_MIN_LENGTH = MOATUser.EMAIL_MIN_LENGTH;
  public final static int EMAIL_MAX_LENGTH = MOATUser.EMAIL_MAX_LENGTH;
  public final static String EMAIL_PATTERN = MOATUser.EMAIL_PATTERN;

  @Id
  @Column(name = "moat_admin_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotNull
  @Column(name = "username")
  private String username;

  @NotNull
  @Column(name = "password")
  private String password;

  @NotNull
  @Column(name = "email")
  private String email;

  public MOATAdmin() {}

  public MOATAdmin(String username, String password) {
    setUsername(username);
    setPassword(password);
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public Long getId() {
    return id;
  }

  public String getUsername() {
    return username;
  }

  public String getPassword() {
    return password;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }
}
