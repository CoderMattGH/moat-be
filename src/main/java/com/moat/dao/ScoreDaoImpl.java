package com.moat.dao;

import com.moat.entity.Score;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

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

  public int deleteAll() {
    logger.info("In deleteAll() in ScoreDaoImpl");

    return em.createQuery("DELETE FROM Score").executeUpdate();
  }

  public int deleteById(Long id) {
    logger.info("In deleteById() in ScoreDaoImpl.");

    return em.createQuery("DELETE FROM Score s WHERE s.id = :id")
        .setParameter("id", id)
        .executeUpdate();
  }

  public int deleteByUserId(Long userId) {
    logger.info("In deleteByUserId() in ScoreDaoImpl.");

    return em.createQuery("DELETE FROM Score s WHERE s.moatUserId.id = :userId")
        .setParameter("userId", userId)
        .executeUpdate();
  }

  public void saveOrUpdate(Score score) {
    logger.info("In save() in ScoreDaoImpl.");

    if (score.getId() == null) {
      logger.info("Persisting score into db.");

      em.persist(score);
    } else {
      logger.info("Merging score into db.");

      em.merge(score);
    }
  }

  public List<Score> selectAll() {
    logger.info("In selectAll() in ScoreDaoImpl.");

    return em.createQuery("SELECT s FROM Score s", Score.class).getResultList();
  }

  public List<Score> selectAllByUserId(Long userId) {
    logger.info("In selectScoresByUserId() in ScoreDaoImpl.");

    return em.createQuery("SELECT s FROM Score s WHERE s.moatUserId.id=:userId",
        Score.class).setParameter("userId", userId).getResultList();
  }

  public List<Score> selectTopTenScoresSorted() {
    logger.info("In findTopTenScoresSorted() in ScoreDaoImpl.");

    return em.createQuery("SELECT s FROM Score s ORDER BY s.score DESC",
        Score.class).setMaxResults(10).getResultList();
  }
}
