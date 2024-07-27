package com.moat.dao;

import com.moat.entity.Score;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.cfg.NotYetImplementedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Repository("scoreDao")
public class ScoreDaoImpl implements ScoreDao {
  private Logger logger = LoggerFactory.getLogger(ScoreDaoImpl.class);

  @PersistenceContext
  private EntityManager em;

  public void save(Score score) {
    logger.info("In save() in ScoreDaoImpl.");

    if (score.getId() == null) {
      em.persist(score);
    } else {
      logger.info("Merging score into db.");
      em.merge(score);
    }
  }

  public void delete(Score score) {
    logger.info("In delete() in ScoreDaoImpl.");

    if (score.getId() == null) {
      logger.error("Cannot delete Score object with no id.");
    } else {
      if (!em.contains(score)) {
        Score result = em.find(Score.class, score.getId());

        if (result == null) {
          logger.info(
              "Cannot remove score with id={} because it cannot be found.",
              score.getId());

          return;
        }

        em.remove(result);
      } else {
        em.remove(score);
      }
    }
  }

  public void deleteAll() {
    logger.info("In deleteAll() in ScoreDaoImpl");

    throw new NotYetImplementedException();
  }

  @Transactional(readOnly = true)
  public List<Score> selectAll() {
    logger.info("In selectAll() in ScoreDaoImpl.");

    return em.createNamedQuery(Score.FIND_ALL, Score.class).getResultList();
  }

  @Transactional(readOnly = true)
  public List<Score> selectTopTenScoresSorted() {
    logger.info("In findTopTenScoresSorted() in ScoreDaoImpl.");

    return em.createNamedQuery(Score.FIND_TOP_TEN, Score.class)
        .setMaxResults(10).getResultList();
  }
}
