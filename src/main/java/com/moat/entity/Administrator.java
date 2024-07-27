package com.moat.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * An Entity class representing an Administrator.
 */
@Entity
@Table(name = "ADMINISTRATOR")
public class Administrator {
  @Id
  @Column(name = "ID")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotNull
  @Column(name = "USERNAME")
  private String username;

  @NotNull
  @Column(name = "PASSWORD")
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
