package com.moat.dao;

import com.moat.dto.AvgScoreDTO;
import com.moat.entity.Score;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import com.moat.util.UtilFunctions;
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
    logger.debug("In deleteAll() in ScoreDaoImpl");

    return em.createQuery("DELETE FROM Score").executeUpdate();
  }

  public int deleteById(Long id) {
    logger.debug("In deleteById() in ScoreDaoImpl.");

    return em.createQuery("DELETE FROM Score s WHERE s.id = :id")
        .setParameter("id", id)
        .executeUpdate();
  }

  public int deleteByUserId(Long userId) {
    logger.debug("In deleteByUserId() in ScoreDaoImpl.");

    return em.createQuery("DELETE FROM Score s WHERE s.moatUserId.id = :userId")
        .setParameter("userId", userId)
        .executeUpdate();
  }

  public void saveOrUpdate(Score score) {
    logger.debug("In save() in ScoreDaoImpl.");

    if (score.getId() == null) {
      em.persist(score);
    } else {
      em.merge(score);
    }
  }

  public List<Score> selectAll() {
    logger.debug("In selectAll() in ScoreDaoImpl.");

    return em.createQuery("SELECT s FROM Score s", Score.class).getResultList();
  }

  public List<Score> selectAllByUserId(Long userId) {
    logger.debug("In selectAllByUserId() in ScoreDaoImpl.");

    return em.createQuery("SELECT s FROM Score s WHERE s.moatUserId.id=:userId",
        Score.class).setParameter("userId", userId).getResultList();
  }

  public List<Score> selectTopTenScoresSorted() {
    logger.debug("In selectTopTenScoresSorted() in ScoreDaoImpl.");

    return em.createQuery("SELECT s FROM Score s ORDER BY s.score DESC",
        Score.class).setMaxResults(10).getResultList();
  }

  public AvgScoreDTO selectAvgScoreByUserId(Long userId)
      throws NoResultException {
    logger.debug("In selectAvgScoreByUserId() in scoreDaoImpl.");

    String query =
        "SELECT AVG(s.score), SUM(s.hits), SUM(s.hits + s.misses), SUM(s.misses), " +
            "u.username FROM Score s JOIN s.moatUserId u " +
            "WHERE u.id = :userId GROUP BY u.username";

    Object[] result = em.createQuery(query, Object[].class)
        .setParameter("userId", userId)
        .getSingleResult();

    Double avgScore = (Double) result[0];
    Long totalHits = (Long) result[1];
    Long totalNotHits = (Long) result[2];
    Long totalMisses = (Long) result[3];
    String username = (String) result[4];

    double avgAccuracy =
        UtilFunctions.getAveragePercentage(totalHits, totalNotHits, true);

    AvgScoreDTO avgScoreDTO = new AvgScoreDTO();
    avgScoreDTO.setUserId(userId);
    avgScoreDTO.setUsername(username);
    avgScoreDTO.setTotalHits(totalHits);
    avgScoreDTO.setTotalNotHits(totalNotHits);
    avgScoreDTO.setTotalMisses(totalMisses);
    avgScoreDTO.setAvgScore(UtilFunctions.roundToTwoDecimalPlaces(avgScore));
    avgScoreDTO.setAvgAccuracy(avgAccuracy);

    return avgScoreDTO;
  }

  public Score selectLatestScoreByUserId(Long userId) throws NoResultException {
    logger.debug("In selectLatestScoreByUserId() in scoreDaoImpl.");

    String query =
        "SELECT s FROM Score s WHERE s.id = (SELECT MAX(s2.id) FROM Score s2 " +
            "WHERE s2.moatUserId.id = :userId)";

    return em.createQuery(query, Score.class)
        .setParameter("userId", userId)
        .getSingleResult();
  }
}
