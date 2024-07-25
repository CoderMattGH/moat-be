package com.moat.service;

import com.moat.entity.Score;


/**
 * A service class for the Leaderboard.
 */
public interface HighScores {
  /**
   * Returns the top ten Scores from the Leaderboard sorted in descending order of Score.
   *
   * @return An array of Score objects or NULL if empty.
   */
  Score[] getTopTenSortedScores();

  /**
   * Checks the supplied Score to see if it should be added to the Leaderboard.
   *
   * @param score A Score object representing the Score to check.
   * @return A boolean indicating if the Score was successfully added to the Leaderboard or not.
   */
  boolean checkAndSaveIfTopTenScore(Score score);

  /**
   * Removes all Scores containing the specified Nickname.
   *
   * @param nickname A String representing the Nickname.
   * @return A boolean indicating if the operation was successful or not. 'False' also indicates
   * that no records were found.
   */
  boolean removeScoresWithNickname(String nickname);

  /**
   * Remove all scores from the Leaderboard.
   */
  void removeAllScores();
}
