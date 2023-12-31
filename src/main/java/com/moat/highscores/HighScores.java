package com.moat.highscores;

import com.moat.entity.Score;

import java.util.ArrayList;

public interface HighScores {
    Score[] getTopTenSortedScores();
    boolean checkAndSaveIfTopTenScore(Score score);
    boolean removeScoresWithNickname(String nickname);
    void removeAllScores();
}
