package com.moat.controller;

import com.moat.service.EndpointService;
import com.moat.service.HighScores;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
public class MainController {
  private final static Logger logger = LoggerFactory.getLogger(MainController.class);

  private final HighScores highScores;
  private final EndpointService endpointService;

  public MainController(HighScores highScores, EndpointService endpointService) {
    logger.info("Constructing MainController.");

    this.highScores = highScores;
    this.endpointService = endpointService;
  }

  @GetMapping(value = "/", produces = "application/json")
  public ResponseEntity<String> getEndpoints() {
    logger.info("In getEndpoints() in MainController.");

    String endpoints;
    try {
      endpoints = endpointService.getEndpoints();
    } catch (Exception e) {
      return new ResponseEntity(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    return new ResponseEntity<>(endpoints, HttpStatus.OK);
  }
}
