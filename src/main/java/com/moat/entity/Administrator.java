package com.moat.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

// TODO: Fix
@Entity
@Table(name = "administrator")
public class Administrator {
  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotNull
  @Column(name = "username")
  private String username;

  @NotNull
  @Column(name = "password")
  private String password;

  public Administrator() {
  }

  public Administrator(String username, String password) {
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

  public String toString() {
    return String.format("ID: %d, Username: %s, Password: %s", id, username,
        password);
  }
}
