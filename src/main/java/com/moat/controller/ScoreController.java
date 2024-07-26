package com.moat.controller;

import com.moat.entity.Score;
import com.moat.service.HighScores;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping(value = "/score")
public class ScoreController {
  private static Logger logger = LoggerFactory.getLogger(ScoreController.class);

  private HighScores highScores;

  public ScoreController(HighScores highScores) {
    logger.info("Constructing ScoreController.");

    this.highScores = highScores;
  }

  @GetMapping("/")
  public ResponseEntity<Score[]> getScores() {
    logger.info("In getScores() in ScoreController.");

    Score[] leaderboard = this.highScores.getTopTenSortedScores();

    if (leaderboard.length == 0) {
      return new ResponseEntity(HttpStatus.NOT_FOUND);
    }

    return new ResponseEntity<>(leaderboard, HttpStatus.OK);
  }

  @PostMapping("/")
  public boolean postScore(@RequestBody Score score) {
    logger.info("In postScore() in ScoreController.");

    boolean result = highScores.checkAndSaveIfTopTenScore(score);

    return result;
  }

  @DeleteMapping("/")
  public void removeScores() {
    logger.info("In removeScores() in ScoreController.");

    highScores.removeAllScores();
  }

  @DeleteMapping("/{nickname}")
  public ResponseEntity deleteScoresByNickname(@PathVariable("nickname") String nickname) {
    logger.info("In deleteScoresByNickname() in ScoreController.");
    logger.info("Removing high scores with nickname: " + nickname + ".");

    boolean result = highScores.removeScoresWithNickname(nickname);

    if (result) {
      return new ResponseEntity(HttpStatus.NOT_FOUND);
    } else {
      return new ResponseEntity(HttpStatus.OK);
    }
  }
}
