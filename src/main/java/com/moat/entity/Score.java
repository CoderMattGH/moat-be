package com.moat.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import com.moat.util.UtilFunctions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "score")
public class Score implements Serializable {
  private static final Logger logger = LoggerFactory.getLogger(Score.class);

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "score_id")
  private Long id;

  @NotNull
  @Column(name = "score")
  private int score;

  @NotNull
  @OneToOne
  @JoinColumn(name = "moat_user_id", referencedColumnName = "moat_user_id")
  private MOATUser moatUserId;

  @NotNull
  @Column(name = "hits")
  private Integer hits;

  @NotNull
  @Column(name = "not_hits")
  private Integer notHits;

  @NotNull
  @Column(name = "misses")
  private Integer misses;

  public Double getAverage() {
    return UtilFunctions.getAveragePercentage(hits, hits + misses, true);
  }

  public Long getId() {
    return this.id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public int getScore() {
    return this.score;
  }

  public void setScore(int score) {
    this.score = score;
  }

  public MOATUser getMoatUserId() {
    return this.moatUserId;
  }

  public void setMoatUserId(MOATUser moatUserId) {
    this.moatUserId = moatUserId;
  }

  public Integer getHits() {
    return hits;
  }

  public void setHits(Integer hits) {
    this.hits = hits;
  }

  public Integer getNotHits() {
    return notHits;
  }

  public void setNotHits(Integer notHits) {
    this.notHits = notHits;
  }

  public Integer getMisses() {
    return misses;
  }

  public void setMisses(Integer misses) {
    this.misses = misses;
  }
}
