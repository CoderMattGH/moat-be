package com.moat.service;

import com.moat.dto.AvgScoreDTO;
import com.moat.dto.ScoreDTO;
import com.moat.entity.Score;
import com.moat.exception.NotFoundException;

import java.util.List;

public interface ScoreService {
  void deleteAll();

  void deleteById(Long id) throws NotFoundException;

  void deleteByUserId(Long userId) throws NotFoundException;

  ScoreDTO saveOrUpdate(ScoreDTO scoreDTO);

  void saveOrUpdate(Score score);

  List<ScoreDTO> selectAll() throws NotFoundException;

  List<ScoreDTO> selectAllByUserId(Long userId) throws NotFoundException;

  AvgScoreDTO getAverageScore(Long userId) throws NotFoundException;

  ScoreDTO getLastScore(Long userId) throws NotFoundException;

  List<ScoreDTO> selectTopTenScores() throws NotFoundException;

  ScoreDTO marshallIntoDTO(Score score);

  List<ScoreDTO> marshallIntoDTO(List<Score> scores);
}
