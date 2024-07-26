package com.moat.service;

import com.moat.entity.Score;
import com.moat.profanityfilter.ProfanityFilterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Iterator;
import java.util.List;

@Component("highScores")
public class HighScoresImpl implements HighScores {
  private final static Logger logger = LoggerFactory.getLogger(HighScoresImpl.class);

  private final ScoreService scoreService;
  private final ProfanityFilterService profanityFilterService;

  public HighScoresImpl(ScoreService scoreService, ProfanityFilterService profanityFilterService) {
    logger.info("Constructing HighScoresImpl.");

    this.profanityFilterService = profanityFilterService;
    this.scoreService = scoreService;
  }

  public Score[] getTopTenSortedScores() {
    logger.info("In getTopTenSortedScores() in HighScoresImpl.");

    List<Score> scores = scoreService.findTopTenScoresSorted();

    if (scores.isEmpty())
      return null;

    Score[] scoresArr = new Score[scores.size()];

    return scores.toArray(scoresArr);
  }

  @Transactional
  public boolean checkAndSaveIfTopTenScore(Score score) {
    logger.info("In checkAndSaveIfTopTenScore() in HighScoresImpl");
    logger.info("Checking if score " + score.getScore() + " is in top ten scores.");

    boolean result = false;

    // Check that nickname does not contain profanity.
//    if (profanityFilterService.isEnabled()) {
//      result = profanityFilterService.isValid(score.getNickname());
//
//      if (!result) {
//        logger.info("Nickname contained a profane word.  Not updating the leaderboard.");
//
//        return false;
//      }
//    }

    List<Score> scores = scoreService.findTopTenScoresSorted();

    if (!scores.isEmpty() && (scores.size() >= 10)) {
      Score minScoreObj = scores.get(scores.size() - 1);
      int minScore = minScoreObj.getScore();

      if (score.getScore() > minScore) {
        // Remove current lowest score.
        scoreService.delete(minScoreObj);

        // Save current score.
        scoreService.save(score);

        return true;
      } else {
        return false;
      }
    } else {
      // If there is space on the leaderboard then save new score.
      scoreService.save(score);

      return true;
    }
  }

  @Transactional
  public boolean removeScoresWithNickname(String nickname) {
    logger.info("In removeScoresWithNickname() in HighScoresImpl");

    if (nickname == null || nickname.isEmpty() || nickname == "") {
      logger.error("Nickname cannot be empty or null");

      return false;
    }

    logger.info("Trying to Remove scores with nickname: " + nickname + ".");

    List<Score> scores = scoreService.findScoresByNickname(nickname);

    if (scores.isEmpty())
      return false;

    Iterator<Score> iterator = scores.iterator();

    while (iterator.hasNext()) {
      Score score = iterator.next();

      logger.info("Removing score with id: " + score.getId() + ".");

      scoreService.delete(score);
    }

    return true;
  }

  @Transactional
  public void removeAllScores() {
    logger.info("Trying to remove all high scores!");

    List<Score> scores = scoreService.findAll();

    Iterator<Score> iterator = scores.iterator();

    while (iterator.hasNext()) {
      Score score = iterator.next();

      logger.info("Deleting score with ID: " + score.getId());
      scoreService.delete(score);
    }
  }
}
