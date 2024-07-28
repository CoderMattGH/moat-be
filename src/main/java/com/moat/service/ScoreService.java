package com.moat.service;

import com.moat.dto.ScoreDTO;
import com.moat.entity.Score;

import java.util.List;

public interface ScoreService {
  List<ScoreDTO> selectAll();

  List<Score> selectTopTenScores();

  void save(Score score);

  void delete(Score score);

  void deleteAll();
}
