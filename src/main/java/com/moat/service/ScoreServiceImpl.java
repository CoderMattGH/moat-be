package com.moat.service;

import com.moat.constant.ValidationMsg;
import com.moat.dao.ScoreDao;
import com.moat.dao.UserDao;
import com.moat.dto.ScoreDTO;
import com.moat.entity.MOATUser;
import com.moat.entity.Score;
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
  private final UserDao userDao;

  public ScoreServiceImpl(ScoreDao scoreDao, UserDao userDao) {
    logger.debug("Constructing ScoreServiceImpl.");

    this.scoreDao = scoreDao;
    this.userDao = userDao;
  }

  @Transactional(readOnly = true)
  public List<ScoreDTO> selectAll() throws NoResultException {
    logger.debug("In selectAll() in ScoreServiceImpl.");

    List<Score> scores = scoreDao.selectAll();

    if (scores.isEmpty()) {
      throw new NoResultException(ValidationMsg.SCORES_NOT_FOUND);
    }

    return marshallIntoDTO(scores);
  }

  @Transactional(readOnly = true)
  public List<ScoreDTO> selectTopTenScores() throws NoResultException {
    logger.debug("In selectTopTenScores() in ScoreServiceImpl.");

    List<Score> scores = scoreDao.selectTopTenScoresSorted();

    if (scores.isEmpty()) {
      throw new NoResultException(ValidationMsg.SCORES_NOT_FOUND);
    }

    return marshallIntoDTO(scores);
  }

  @Transactional(readOnly = true)
  public List<ScoreDTO> selectAllByUserId(Long userId)
      throws NoResultException {
    logger.debug("In selectAllByUserId() in ScoreServiceImpl.");

    // Check user exists
    try {
      userDao.selectById(userId);
    } catch (NoResultException e) {
      throw new NoResultException(ValidationMsg.USER_DOES_NOT_EXIST);
    }

    List<Score> scores = scoreDao.selectAllByUserId(userId);

    if (scores.isEmpty()) {
      throw new NoResultException(ValidationMsg.SCORES_NOT_FOUND);
    }

    return marshallIntoDTO(scores);
  }

  public void saveOrUpdate(Score score) {
    logger.debug("In saveOrUpdate() in ScoreServiceImpl.");

    scoreDao.saveOrUpdate(score);
  }

  public ScoreDTO save(ScoreDTO scoreDTO) throws NoResultException {
    MOATUser user;
    try {
      user = userDao.selectById(scoreDTO.getUserId());
    } catch (NoResultException e) {
      throw new NoResultException(ValidationMsg.USER_DOES_NOT_EXIST);
    }

    Score score = new Score();
    score.setScore(scoreDTO.getScore());
    score.setMoatUserId(user);
    score.setHits(scoreDTO.getHits());
    score.setNotHits(scoreDTO.getNotHits());
    score.setMisses(scoreDTO.getMisses());

    scoreDao.saveOrUpdate(score);

    return marshallIntoDTO(score);
  }

  public void deleteAll() throws NoResultException {
    logger.debug("In deleteAll() in ScoreServiceImpl.");

    int result = scoreDao.deleteAll();

    if (result == 0) {
      throw new NoResultException(ValidationMsg.SCORES_NOT_FOUND);
    }
  }

  public void deleteById(Long id) throws NoResultException {
    logger.debug("In deleteById() in ScoreServiceImpl.");

    int result = scoreDao.deleteById(id);

    if (result == 0) {
      throw new NoResultException(ValidationMsg.SCORE_DOES_NOT_EXIST);
    }
  }

  public void deleteByUserId(Long userId) throws NoResultException {
    logger.debug("In deleteByUserId() in ScoreServiceImpl.");

    try {
      // Check user exists
      userDao.selectById(userId);
    } catch (NoResultException e) {
      throw new NoResultException(ValidationMsg.USER_DOES_NOT_EXIST);
    }

    int result = scoreDao.deleteByUserId(userId);

    if (result == 0) {
      throw new NoResultException(ValidationMsg.SCORES_NOT_FOUND);
    }
  }

  @Transactional(propagation = Propagation.NOT_SUPPORTED)
  public ScoreDTO marshallIntoDTO(Score score) {
    ScoreDTO dto = new ScoreDTO();

    dto.setId(score.getId());
    dto.setScore(score.getScore());
    dto.setUsername(score.getMoatUserId().getUsername());
    dto.setUserId(score.getMoatUserId().getId());
    dto.setHits(score.getHits());
    dto.setNotHits(score.getNotHits());
    dto.setMisses(score.getMisses());

    return dto;
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
