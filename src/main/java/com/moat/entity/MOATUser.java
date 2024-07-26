package com.moat.entity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "moat_user")
public class MOATUser implements Serializable {
  static Logger logger = LoggerFactory.getLogger(MOATUser.class);

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
  @Column(name = "ID")
  private Long id;

  @Column(name = "USERNAME")
  private String username;

  @Column(name = "PASSWORD")
  private String password;

  @Column(name = "EMAIL")
  private String email;

  @Column(name = "VERIFIED")
  private boolean verified;

  @Column(name = "BANNED")
  private boolean banned;

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

  public boolean isVerified() {
    return verified;
  }

  public void setVerified(boolean verified) {
    this.verified = verified;
  }

  public boolean isBanned() {
    return banned;
  }

  public void setBanned(boolean banned) {
    this.banned = banned;
  }
}
