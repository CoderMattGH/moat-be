package com.moat.dao;

import com.moat.entity.Score;

import java.util.List;

public interface ScoreDao {
  int deleteAll();

  int deleteById(Long id);

  int deleteByUserId(Long userId);

  void saveOrUpdate(Score score);

  List<Score> selectAll();

  List<Score> selectAllByUserId(Long userId);

  List<Score> selectTopTenScoresSorted();
}
