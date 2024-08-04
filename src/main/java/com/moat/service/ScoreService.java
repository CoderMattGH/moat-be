package com.moat.service;

import com.moat.dto.AvgScoreDTO;
import com.moat.dto.ScoreDTO;
import com.moat.entity.Score;

import javax.persistence.NoResultException;
import java.util.List;

public interface ScoreService {
  void deleteAll();

  void deleteById(Long id) throws NoResultException;

  void deleteByUserId(Long userId) throws NoResultException;

  ScoreDTO save(ScoreDTO scoreDTO);

  void saveOrUpdate(Score score);

  List<ScoreDTO> selectAll() throws NoResultException;

  List<ScoreDTO> selectAllByUserId(Long userId) throws NoResultException;

  AvgScoreDTO getAverageScore(Long userId) throws NoResultException;

  ScoreDTO getLastScore(Long userId) throws NoResultException;

  List<ScoreDTO> selectTopTenScores() throws NoResultException;

  ScoreDTO marshallIntoDTO(Score score);

  List<ScoreDTO> marshallIntoDTO(List<Score> scores);
}
