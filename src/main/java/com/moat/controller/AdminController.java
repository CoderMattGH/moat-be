package com.moat.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping(value = "/admin")
public class AdminController {
  private final static Logger logger =
      LoggerFactory.getLogger(AdminController.class);

  public AdminController() {
    logger.info("Construcing AdminController.");
  }

  /**
   * This method is left intentionally blank as the method is intercepted and
   * the logic is injected by Spring Security.
   */
  @PostMapping("/check-login/")
  public void checkLogin() {
    logger.info("Checking administrator login credentials.");
  }
}