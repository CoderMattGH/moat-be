package com.moat.service;

import com.moat.dao.ScoreDao;
import com.moat.dto.ScoreDTO;
import com.moat.entity.Score;
import com.moat.profanityfilter.ProfanityFilterService;
import org.hibernate.cfg.NotYetImplementedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.NoResultException;
import java.util.ArrayList;
import java.util.List;

@Transactional
@Service("scoreService")
public class ScoreServiceImpl implements ScoreService {
  private final static Logger logger =
      LoggerFactory.getLogger(ScoreServiceImpl.class);

  private final ScoreDao scoreDao;
  private final ProfanityFilterService profanityFilterService;

  public ScoreServiceImpl(ScoreDao scoreDao,
      ProfanityFilterService profanityFilterService) {
    logger.info("Constructing ScoreServiceImpl.");

    this.scoreDao = scoreDao;
    this.profanityFilterService = profanityFilterService;
  }

  @Transactional(readOnly = true)
  public List<Score> selectAll() {
    logger.info("In selectAll() in ScoreServiceImpl.");

    return scoreDao.selectAll();
  }

  @Transactional(readOnly = true)
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

    try {
      scoreDao.delete(score);
    } catch (NoResultException e) {
      throw new NoResultException("Score doesn't exist!");
    }
  }

  // TODO: Make one query
  public void deleteAll() {
    logger.info("In deleteAll() in ScoreServiceImpl.");

    throw new NotYetImplementedException();
  }

  @Transactional(propagation = Propagation.NOT_SUPPORTED)
  public ScoreDTO marshallIntoDTO(Score score) {
    return new ScoreDTO(score.getId(), score.getScore(),
        score.getMoatUserId().getId(), score.getMoatUserId().getUsername());
  }

  @Transactional(propagation = Propagation.NOT_SUPPORTED)
  public List<ScoreDTO> marshallIntoDTO(List<Score> scores) {
    List<ScoreDTO> dtos = new ArrayList<>();

    for (Score score : scores) {
      dtos.add(marshallIntoDTO(score));
    }

    return dtos;
  }
}
