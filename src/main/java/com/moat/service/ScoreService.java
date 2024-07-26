package com.moat.service;

import com.moat.entity.Score;

import java.util.List;

/**
 * A class offering Score Service functionality.
 */
public interface ScoreService {
  /**
   * Returns all Score objects found in the Database.
   */
  List<Score> findAll();

  /**
   * Gets the top ten Scores from the Database sorted in descending order.
   *
   * @return A List<Score> object containing the Scores.
   */
  List<Score> findTopTenScoresSorted();

  /**
   * Attempts to save the current Score in the Database.
   *
   * @param score A Score object representing the Score to save.
   */
  void save(Score score);

  /**
   * Attempts to delete the specified Score from the Database.
   *
   * @param score A Score object representing the Score to delete.
   */
  void delete(Score score);

  /**
   * Finds all Scores with the supplied Nickname. Note that this function does not return 'NULL',
   * but will instead return an empty list if no results are found.
   *
   * @param nickname A String representing the Nickname.
   * @return A List<Score> object containing all the Scores found.
   */
  List<Score> findScoresByNickname(String nickname);
}
