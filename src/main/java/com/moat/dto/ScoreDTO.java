package com.moat.dto;

public class ScoreDTO {
  private Long id;
  private int score;
  private Long userId;
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
