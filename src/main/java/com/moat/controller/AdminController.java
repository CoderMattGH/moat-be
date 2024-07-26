package com.moat.controller;

import com.moat.service.HighScores;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

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
   * This method is left intentionally blank as the method is intercepted and the logic is injected
   * by Spring Security.
   */
  @PostMapping("/check-login/")
  public void checkLogin() {
    logger.info("Checking administrator login credentials.");
  }
}