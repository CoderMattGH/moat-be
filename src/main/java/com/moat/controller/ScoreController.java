package com.moat.controller;

import com.moat.constant.ValidationMsg;
import com.moat.dto.ScoreDTO;
import com.moat.responsewrapper.DynamicResponseWrapperFactory;
import com.moat.service.ScoreService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.persistence.NoResultException;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.List;

import static java.lang.String.format;

@CrossOrigin
@RestController
@Validated
@RequestMapping(value = "/score")
public class ScoreController {
  private static final Logger logger =
      LoggerFactory.getLogger(ScoreController.class);

  private final ScoreService scoreService;
  private final DynamicResponseWrapperFactory resFact;

  public ScoreController(ScoreService scoreService,
      DynamicResponseWrapperFactory resFact) {
    logger.debug("Constructing ScoreController.");

    this.scoreService = scoreService;
    this.resFact = resFact;
  }

  @DeleteMapping("/")
  public ResponseEntity<?> deleteAllScores() {
    logger.debug("In deleteAllScores() in ScoreController.");

    try {
      scoreService.deleteAll();
    } catch (Exception e) {
      return resFact.build("message", ValidationMsg.ERROR_DELETING_SCORES,
          HttpStatus.INTERNAL_SERVER_ERROR);
    }

    return ResponseEntity.status(HttpStatus.OK).build();
  }

  @DeleteMapping("/{userId}")
  public ResponseEntity<?> deleteScoresByUserId(
      @PathVariable("userId") @NotNull @Positive Long userId) {
    logger.debug("In deleteScoresByNickname() in ScoreController.");
    logger.info(format("Removing high scores where userId: %d.", userId));

    try {
      scoreService.deleteByUserId(userId);
    } catch (NoResultException e) {
      return resFact.build("message", e.getMessage(), HttpStatus.NOT_FOUND);
    } catch (Exception e) {
      return resFact.build("message", ValidationMsg.ERROR_DELETING_SCORES,
          HttpStatus.INTERNAL_SERVER_ERROR);
    }

    return ResponseEntity.status(HttpStatus.OK).build();
  }

  @GetMapping("/")
  public ResponseEntity<?> getScores() {
    logger.debug("In getScores() in ScoreController.");

    List<ScoreDTO> scores;

    try {
      scores = this.scoreService.selectAll();
    } catch (NoResultException e) {
      return resFact.build("message", e.getMessage(), HttpStatus.NOT_FOUND);
    } catch (Exception e) {
      return resFact.build("message", ValidationMsg.ERROR_GETTING_SCORES,
          HttpStatus.INTERNAL_SERVER_ERROR);
    }

    return resFact.build("scores", scores, HttpStatus.OK);
  }

  @GetMapping("/{userId}")
  public ResponseEntity<?> getScoresByUserId(
      @PathVariable("userId") @NotNull @Positive Long userId) {
    logger.debug("In getScoresByUserId() in ScoreController.");
    logger.info(format("Getting all scores where userId: %d.", userId));

    List<ScoreDTO> scores;
    try {
      scores = scoreService.selectAllByUserId(userId);
    } catch (NoResultException e) {
      return resFact.build("message", e.getMessage(), HttpStatus.NOT_FOUND);
    } catch (Exception e) {
      return resFact.build("message", ValidationMsg.ERROR_GETTING_SCORES,
          HttpStatus.INTERNAL_SERVER_ERROR);
    }

    return resFact.build("scores", scores, HttpStatus.OK);
  }

  @PostMapping("/")
  public ResponseEntity<?> postScore(@RequestBody @Valid ScoreDTO scoreDTO) {
    logger.debug("In postScore() in ScoreController.");

    ScoreDTO savedScore;
    try {
      savedScore = scoreService.save(scoreDTO);
    } catch (NoResultException e) {
      return resFact.build("message", e.getMessage(), HttpStatus.NOT_FOUND);
    } catch (Exception e) {
      return resFact.build("message", ValidationMsg.ERROR_POSTING_SCORE,
          HttpStatus.INTERNAL_SERVER_ERROR);
    }

    return resFact.build("score", savedScore, HttpStatus.CREATED);
  }
}
