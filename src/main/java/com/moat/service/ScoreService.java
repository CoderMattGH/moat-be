package com.moat.service;

import com.moat.entity.Score;

import java.util.List;

public interface ScoreService{
    List<Score> findAll();
    List<Score> findTopTenScoresSorted();
    void save(Score score);
    void delete(Score score);
    List<Score> findScoresByNickname(String nickname);
}
