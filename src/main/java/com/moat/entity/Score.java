package com.moat.entity;

import javax.persistence.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

@Entity
@Table(name = "score")
public class Score implements Serializable {
  private static final Logger logger = LoggerFactory.getLogger(Score.class);

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "score_id")
  private Long id;

  @Column(name = "score")
  private int score;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "moat_user_id", referencedColumnName = "moat_user_id")
  private MOATUser moatUserId;

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
}
