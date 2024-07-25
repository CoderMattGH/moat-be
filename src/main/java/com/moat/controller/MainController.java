package com.moat.controller;

import com.moat.constant.Constants;
import com.moat.service.EndpointService;
import com.moat.service.EndpointServiceImpl;
import com.moat.service.HighScores;
import com.moat.entity.Score;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.nio.charset.StandardCharsets;

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

  /**
   * @return A JSON object of the API endpoints.
   */
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

  /**
   * Returns the leaderboard as a sorted array. Returns 'null' if there are no scores in the
   * database.
   *
   * @return An array of Score objects.
   */
  @GetMapping("/scores/")
  public ResponseEntity<Score[]> getLeaderBoard() {
    logger.info("In getLeaderBoard() in MainController.");

    Score[] leaderboard = this.highScores.getTopTenSortedScores();

    if (leaderboard.length == 0) {
      return new ResponseEntity(HttpStatus.NOT_FOUND);
    }

    return new ResponseEntity<>(leaderboard, HttpStatus.OK);
  }

  /**
   * Receives a Score object, and checks to see if the player score is big enough to be entered into
   * the leaderboard.
   *
   * @param score A Score object.
   * @return Returns true if it is a new high score.  False otherwise.
   */
  @PostMapping("/score/")
  public boolean sendScore(@RequestBody Score score) {
    logger.info("In sendScore() in MainController.");

    boolean result = highScores.checkAndSaveIfTopTenScore(score);

    return result;
  }
}
