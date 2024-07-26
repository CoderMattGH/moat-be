package com.moat.entity;

import javax.persistence.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

@Entity
@Table(name = "score")
@NamedQueries({
    @NamedQuery(name = Score.FIND_ALL, query = "select s from Score s"),
    @NamedQuery(name = Score.FIND_TOP_TEN,
        query = "select s from Score s ORDER BY s.score DESC")
})
public class Score implements Serializable {
  static Logger logger = LoggerFactory.getLogger(Score.class);

  public static final String FIND_ALL = "Score.findAll";
  public static final String FIND_TOP_TEN = "Score.findTopTen";

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "score_id")
  private Long id;

  @Column(name = "score")
  private int score;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "moat_user_id", referencedColumnName = "moat_user_id")
  private MOATUser mOATUserId;

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
    if (score < 0)
      throw new IllegalArgumentException("Score cannot be below 0.");
    else
      this.score = score;
  }

  public MOATUser getMOATUserId() {
    return this.mOATUserId;
  }

  public void setMOATUserId(MOATUser mOATUserId) {
    this.mOATUserId = mOATUserId;
  }
}
