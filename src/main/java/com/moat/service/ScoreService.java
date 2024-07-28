package com.moat.service;

import com.moat.dto.ScoreDTO;
import com.moat.entity.Score;

import java.util.List;

public interface ScoreService {
  List<Score> selectAll();

  List<Score> selectTopTenScores();

  void save(Score score);

  void delete(Score score);

  void deleteAll();

  ScoreDTO marshallIntoDTO(Score score);

  List<ScoreDTO> marshallIntoDTO(List<Score> scores);
}
