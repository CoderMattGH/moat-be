package com.moat.controller;

import com.moat.dto.NicknameDTO;
import com.moat.service.HighScores;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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

  /**
   * Removes all Scores on the Leaderboard with the supplied Nickname.
   *
   * @param nicknameDTO A NicknameDTO object representing the Nickname.
   */
  @PostMapping("/remove-scores-with-nickname/")
  public void removeHighScoresWithNickname(@Valid @RequestBody NicknameDTO nicknameDTO) {
    logger.info("Removing high scores with nickname: " + nicknameDTO.getNickname() + ".");

    boolean result = highScores.removeScoresWithNickname(nicknameDTO.getNickname());
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
  @PostMapping("/remove-all-scores/")
  public void removeAllScores() {
    logger.info("Removing all high scores!");

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
