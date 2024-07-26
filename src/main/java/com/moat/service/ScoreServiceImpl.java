package com.moat.service;

import com.moat.entity.Score;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("scoreService")
@Transactional
@Repository
public class ScoreServiceImpl implements ScoreService {
  Logger logger = LoggerFactory.getLogger(ScoreServiceImpl.class);

  @PersistenceContext
  private EntityManager em;

  public void save(Score score) {
    logger.info("In save() in ScoreServiceImpl.");

    if (score.getId() == null) {
      em.persist(score);
    } else {
      logger.info("Merging score into db.");
      em.merge(score);
    }
  }

  public void delete(Score score) {
    logger.info("In delete() in ScoreServiceImpl.");

    if (score.getId() == null) {
      logger.error("Cannot delete Score object with no id.");
    } else {
      if (!em.contains(score)) {
        Score result = em.find(Score.class, score.getId());

        if (result == null) {
          logger.info("Cannot remove score with id={} because it cannot be found.",
              score.getId());

          return;
        }

        em.remove(result);
      } else {
        em.remove(score);
      }
    }
  }

  @Transactional(readOnly = true)
  public List<Score> findAll() {
    logger.info("In findAll() in ScoreServiceImpl.");

    return em.createNamedQuery(Score.FIND_ALL, Score.class).getResultList();
  }

  @Transactional(readOnly = true)
  public List<Score> findTopTenScoresSorted() {
    logger.info("In findTopTenScoresSorted() in ScoreServiceImpl.");

    return em.createNamedQuery(Score.FIND_TOP_TEN, Score.class)
        .setMaxResults(10).getResultList();
  }

  public List<Score> findScoresByNickname(String nickname) {
    logger.info("In findScoresByNickname() in ScoreServiceImpl.");
    logger.info("Finding scores by nickname: " + nickname + ".");

    nickname = nickname.toUpperCase();

    return em.createQuery("SELECT s FROM Score s WHERE upper(s.nickname) = :nickname",
        Score.class).setParameter("nickname", nickname).getResultList();
  }
}
