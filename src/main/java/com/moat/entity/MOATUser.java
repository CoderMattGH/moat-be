package com.moat.entity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@Table(name = "moat_user")
public class MOATUser implements Serializable {
  private final static Logger logger = LoggerFactory.getLogger(MOATUser.class);

  public final static int USERNAME_MIN_LENGTH = 5;
  public final static int USERNAME_MAX_LENGTH = 15;
  public final static String USERNAME_PATTERN = "";

  public final static int PASSWORD_MIN_LENGTH = 5;
  public final static int PASSWORD_MAX_LENGTH = 15;
  public final static String PASSWORD_PATTERN = "";

  public final static int EMAIL_MIN_LENGTH = 4;
  public final static int EMAIL_MAX_LENGTH = 30;
  public final static String EMAIL_PATTERN = "";

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "moat_user_id")
  private Long id;

  @NotNull(message = "Username cannot be null!")
  @NotEmpty(message = "Username cannot be empty!")
  @Column(name = "username")
  private String username;

  @NotNull(message = "Password cannot be null!")
  @NotEmpty(message = "Password cannot be empty!")
  @Column(name = "password")
  private String password;

  @NotNull(message = "Email cannot be null!")
  @NotEmpty(message = "Email cannot be empty!")
  @Column(name = "email")
  private String email;

  @Column(name = "verified", columnDefinition = "boolean default false")
  private Boolean verified = false;

  @Column(name = "banned", columnDefinition = "boolean default false")
  private Boolean banned = false;

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

  public Boolean isVerified() {
    return verified;
  }

  public void setVerified(Boolean verified) {
    this.verified = verified;
  }

  public Boolean isBanned() {
    return banned;
  }

  public void setBanned(Boolean banned) {
    this.banned = banned;
  }
}
