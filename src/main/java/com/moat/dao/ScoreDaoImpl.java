package com.moat.dao;

import com.moat.entity.Score;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import org.hibernate.cfg.NotYetImplementedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("scoreDao")
public class ScoreDaoImpl implements ScoreDao {
  private final static Logger logger =
      LoggerFactory.getLogger(ScoreDaoImpl.class);

  @PersistenceContext
  private EntityManager em;

  public void save(Score score) {
    logger.info("In save() in ScoreDaoImpl.");

    if (score.getId() == null) {
      logger.info("Persisting score into db.");

      em.persist(score);
    } else {
      logger.info("Merging score into db.");

      em.merge(score);
    }
  }

  public void delete(Score score) throws NoResultException {
    logger.info("In delete() in ScoreDaoImpl.");

    if (!em.contains(score)) {
      Score result = em.find(Score.class, score.getId());

      if (result == null) {
        throw new NoResultException("Score doesn't exist!");
      }

      em.remove(result);
    } else {
      em.remove(score);
    }
  }

  public void deleteAll() {
    logger.info("In deleteAll() in ScoreDaoImpl");

    em.createQuery("DELETE FROM Score").executeUpdate();
  }

  public List<Score> selectAll() {
    logger.info("In selectAll() in ScoreDaoImpl.");

    return em.createQuery("SELECT s FROM Score s", Score.class).getResultList();
  }

  public List<Score> selectTopTenScoresSorted() {
    logger.info("In findTopTenScoresSorted() in ScoreDaoImpl.");

    return em.createQuery("SELECT s FROM Score s ORDER BY s.score DESC",
        Score.class).setMaxResults(10).getResultList();
  }
}
