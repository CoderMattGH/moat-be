package com.moat.dao;

import com.moat.entity.Score;

import javax.persistence.NoResultException;
import java.util.List;

public interface ScoreDao {
  List<Score> selectAll();

  List<Score> selectTopTenScoresSorted();

  void save(Score score);

  void delete(Score score) throws NoResultException;
}
