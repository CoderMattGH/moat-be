package com.moat.service;

import com.moat.dto.ScoreDTO;
import com.moat.entity.Score;
import com.moat.exception.MOATValidationException;

import java.util.List;

public interface ScoreService {
  List<Score> selectAll();

  List<Score> selectTopTenScores();

  void save(Score score);

  ScoreDTO save(ScoreDTO scoreDTO);

  void delete(Score score);

  void deleteAll();

  ScoreDTO marshallIntoDTO(Score score);

  List<ScoreDTO> marshallIntoDTO(List<Score> scores);
}
