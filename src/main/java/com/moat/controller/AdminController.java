package com.moat.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// TODO: Fix
@CrossOrigin
@RestController
@RequestMapping(value = "/admin")
public class AdminController {
  private final static Logger logger =
      LoggerFactory.getLogger(AdminController.class);

  public AdminController() {logger.debug("Constructing AdminController.");}

  // TODO: Delete in production
  @PostMapping("/test-auth/")
  public ResponseEntity<?> testAuth() {
    logger.debug("Checking administrator login credentials.");

    return ResponseEntity.status(HttpStatus.OK).build();
  }
}