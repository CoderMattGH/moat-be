package com.moat.controller;

import com.moat.service.HighScores;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

/**
 * AdminController class for providing Administrative functionality.
 */
@CrossOrigin
@RestController
@RequestMapping(value = "/admin")
public class AdminController {
  private static Logger logger = LoggerFactory.getLogger(AdminController.class);

  private HighScores highScores;

  public AdminController(HighScores highScores) {
    logger.info("Construcing AdminController.");

    this.highScores = highScores;
  }

  @DeleteMapping("/scores/{nickname}")
  public void deleteScoresByNickname(@PathVariable("nickname") String nickname) {
    logger.info("In deleteScoresByNickname() in AdminController");
    logger.info("Removing high scores with nickname: " + nickname + ".");

    // Validate nickname?
    boolean result = highScores.removeScoresWithNickname(nickname);
  }

  /**
   * Unimplemented method.
   */
  public void removeHighScoresById(int id) {
    throw new UnsupportedOperationException();
  }

  /**
   * Removes all Scores from the Leaderboard.
   */
  @DeleteMapping("/scores/")
  public void removeScores() {
    logger.info("In remoeveScores() in AdminController.");

    highScores.removeAllScores();
  }

  /**
   * This method is left intentionally blank as the method is intercepted and the logic is injected
   * by Spring Security.
   */
  @PostMapping("/check-login/")
  public void checkLogin() {
    logger.info("Checking administrator login credentials.");
  }
}
