package com.moat.controller;

import com.moat.constant.ValidationMsg;
import com.moat.dto.AvgScoreDTO;
import com.moat.dto.ScoreDTO;
import com.moat.responsewrapper.DynamicResponseWrapperFactory;
import com.moat.service.ScoreService;
import com.moat.validator.group.SaveScoreGroup;
import com.moat.validator.score.UserIdValid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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

    scoreService.deleteAll();

    return ResponseEntity.status(HttpStatus.OK).build();
  }

  @DeleteMapping("/{userId}/")
  public ResponseEntity<?> deleteScoresByUserId(
      @PathVariable @UserIdValid Long userId) {
    logger.debug("In deleteScoresByNickname() in ScoreController.");
    logger.info(format("Removing high scores where userId: %d.", userId));

    scoreService.deleteByUserId(userId);

    return ResponseEntity.status(HttpStatus.OK).build();
  }

  @GetMapping("/")
  public ResponseEntity<?> getScores() {
    logger.debug("In getScores() in ScoreController.");

    List<ScoreDTO> scores = this.scoreService.selectAll();

    return resFact.build("scores", scores, HttpStatus.OK);
  }

  @GetMapping("/top-ten/")
  public ResponseEntity<?> getTopTenScores() {
    logger.debug("In getTopTenScores() in ScoreController.");

    List<ScoreDTO> scores = this.scoreService.selectTopTenScores();

    return resFact.build("scores", scores, HttpStatus.OK);
  }

  @GetMapping("/{userId}/")
  public ResponseEntity<?> getScoresByUserId(
      @PathVariable @UserIdValid Long userId) {
    logger.debug("In getScoresByUserId() in ScoreController.");
    logger.info(format("Getting all scores where userId: %d.", userId));

    List<ScoreDTO> scores = scoreService.selectAllByUserId(userId);

    return resFact.build("scores", scores, HttpStatus.OK);
  }

  @GetMapping("/avg/{userId}/")
  public ResponseEntity<?> getAvgScoreByUserId(
      @PathVariable @UserIdValid Long userId) {
    logger.debug("In getAvgScoreByUserId() in ScoreController.");

    AvgScoreDTO avgScore = scoreService.getAverageScore(userId);

    return resFact.build("score", avgScore, HttpStatus.OK);
  }

  @GetMapping("/last/{userId}/")
  public ResponseEntity<?> getLastScoreByUserId(
      @PathVariable @UserIdValid Long userId) {
    logger.debug("In getLastScoreByUserId in ScoreController.");

    ScoreDTO lastScore = scoreService.getLastScore(userId);

    return resFact.build("score", lastScore, HttpStatus.OK);
  }

  @PostMapping("/")
  @PreAuthorize("#scoreDTO.userId == principal.id or hasRole('ADMIN')")
  public ResponseEntity<?> postScore(
      @RequestBody @Validated(SaveScoreGroup.class) ScoreDTO scoreDTO) {
    logger.debug("In postScore() in ScoreController.");

    ScoreDTO savedScore = scoreService.saveOrUpdate(scoreDTO);

    return resFact.build("score", savedScore, HttpStatus.CREATED);
  }
}
