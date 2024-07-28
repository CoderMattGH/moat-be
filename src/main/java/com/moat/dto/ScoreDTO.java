package com.moat.dto;

import com.moat.entity.MOATUser;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

public class ScoreDTO {
  @Null
  private Long id;

  @NotNull(message = "score cannot be null!")
  @Min(value = 0, message = "Score must be bigger than 0!")
  private int score;

  @NotNull(message = "userId cannot be null!")
  @Min(value = 1, message = "userId cannot be less than 1!")
  private Long userId;

  //  @NotNull(message = "username cannot be null!")
  //  @Length(min = MOATUser.USERNAME_MIN_LENGTH,
  //      max = MOATUser.USERNAME_MAX_LENGTH,
  //      message = "user must be between " + MOATUser.USERNAME_MIN_LENGTH +
  //          " and " + MOATUser.USERNAME_MAX_LENGTH + " characters in length!")
  @Null
  private String username;

  public ScoreDTO() {
  }

  public ScoreDTO(Long id, int score, Long userId, String username) {
    this.id = id;
    this.score = score;
    this.userId = userId;
    this.username = username;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public int getScore() {
    return score;
  }

  public void setScore(int score) {
    this.score = score;
  }

  public Long getUserId() {
    return userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }
}
