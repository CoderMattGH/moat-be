package com.moat.service;

import com.moat.dao.ScoreDao;
import com.moat.entity.Score;
import com.moat.profanityfilter.ProfanityFilterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Iterator;
import java.util.List;

@Service("scoreService")
public class ScoreServiceImpl implements ScoreService {
  private Logger logger = LoggerFactory.getLogger(ScoreServiceImpl.class);

  ScoreDao scoreDao;
  ProfanityFilterService profanityFilterService;

  public ScoreServiceImpl(ScoreDao scoreDao,
                          ProfanityFilterService profanityFilterService) {
    logger.info("Constructing ScoreServiceImpl.");

    this.scoreDao = scoreDao;
    this.profanityFilterService = profanityFilterService;
  }

  public List<Score> selectAll() {
    logger.info("In selectAll() in ScoreServiceImpl.");

    return scoreDao.selectAll();
  }

  public List<Score> selectTopTenScores() {
    logger.info("In selectTopTenScores() in ScoreServiceImpl.");

    return scoreDao.selectTopTenScoresSorted();
  }

  // TODO: Validation
  public void save(Score score) {
    logger.info("In save() in ScoreServiceImpl.");

    scoreDao.save(score);
  }

  // TODO: Validation
  public void delete(Score score) {
    logger.info("In delete() in ScoreServiceImpl.");

    scoreDao.delete(score);
  }

  // TODO: Make one query
  @Transactional
  public void deleteAll() {
    logger.info("In deleteAll() in ScoreServiceImpl.");

    List<Score> scores = scoreDao.selectAll();

    Iterator<Score> iterator = scores.iterator();

    while (iterator.hasNext()) {
      Score score = iterator.next();

      logger.info("Deleting score with ID: " + score.getId());
      scoreDao.delete(score);
    }
  }
}
