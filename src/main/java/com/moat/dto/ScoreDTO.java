package com.moat.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.moat.validator.group.SaveScoreGroup;
import com.moat.validator.misc.IdValid;
import com.moat.validator.misc.UsernameValid;
import com.moat.validator.score.ScoreValid;
import com.moat.validator.score.UserIdValid;

import javax.validation.groups.Default;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ScoreDTO {
  @IdValid
  private Long id;

  @ScoreValid(groups = {SaveScoreGroup.class, Default.class})
  private Integer score;

  @UserIdValid(groups = {SaveScoreGroup.class, Default.class})
  private Long userId;

  @UsernameValid
  private String username;

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
