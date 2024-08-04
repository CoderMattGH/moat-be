package com.moat.dao;

import com.moat.dto.AvgScoreDTO;
import com.moat.entity.Score;

import javax.persistence.NoResultException;
import java.util.List;

public interface ScoreDao {
  int deleteAll();

  int deleteById(Long id);

  int deleteByUserId(Long userId);

  void saveOrUpdate(Score score);

  List<Score> selectAll();

  List<Score> selectAllByUserId(Long userId);

  AvgScoreDTO selectAvgScoreByUserId(Long userId) throws NoResultException;

  Score selectLatestScoreByUserId(Long userId) throws NoResultException;

  List<Score> selectTopTenScoresSorted();
}
