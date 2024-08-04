package com.moat.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class AvgScoreDTO {
  private Long userId;
  private Long totalHits;
  private Long totalNotHits;
  private Long totalMisses;
  private Double avgScore;
  private Double avgAccuracy;

  public AvgScoreDTO() {}

  public Long getUserId() {
    return userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }

  public Double getAvgScore() {
    return avgScore;
  }

  public void setAvgScore(Double avgScore) {
    this.avgScore = avgScore;
  }

  public Double getAvgAccuracy() {
    return avgAccuracy;
  }

  public void setAvgAccuracy(Double avgAccuracy) {
    this.avgAccuracy = avgAccuracy;
  }

  public Long getTotalHits() {
    return totalHits;
  }

  public void setTotalHits(Long totalHits) {
    this.totalHits = totalHits;
  }

  public Long getTotalNotHits() {
    return totalNotHits;
  }

  public void setTotalNotHits(Long totalNotHits) {
    this.totalNotHits = totalNotHits;
  }

  public Long getTotalMisses() {
    return totalMisses;
  }

  public void setTotalMisses(Long totalMisses) {
    this.totalMisses = totalMisses;
  }
}
