package com.moat.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.moat.validator.score.ScoreValid;
import com.moat.validator.score.UserIdValid;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ScoreDTO {
  private Long id;

  @ScoreValid
  private Integer score;

  @UserIdValid
  private Long userId;

  private String username;

  public ScoreDTO() {}

  public ScoreDTO(Integer score, Long userId) {
    this.score = score;
    this.userId = userId;
  }

  public ScoreDTO(Integer score, Long userId, String username) {
    this.score = score;
    this.userId = userId;
    this.username = username;
  }

  public ScoreDTO(Long id, Integer score, Long userId, String username) {
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

  public Integer getScore() {
    return score;
  }

  public void setScore(Integer score) {
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
