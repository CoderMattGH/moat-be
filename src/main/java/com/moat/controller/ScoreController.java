package com.moat.controller;

import com.moat.dto.ScoreDTO;
import com.moat.entity.Score;
import com.moat.exception.MOATValidationException;
import com.moat.responsewrapper.DynamicResponseWrapperFactory;
import com.moat.service.ScoreService;
import org.hibernate.cfg.NotYetImplementedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping(value = "/score")
public class ScoreController {
  private static final Logger logger =
      LoggerFactory.getLogger(ScoreController.class);

  private final ScoreService scoreService;
  private final DynamicResponseWrapperFactory resFact;

  public ScoreController(ScoreService scoreService,
      DynamicResponseWrapperFactory resFact) {
    logger.info("Constructing ScoreController.");

    this.scoreService = scoreService;
    this.resFact = resFact;
  }

  @GetMapping("/")
  public ResponseEntity<?> getScores() {
    logger.info("In getScores() in ScoreController.");

    List<Score> scores = this.scoreService.selectAll();
    if (scores.isEmpty()) {
      return resFact.build("message", "No scores found!", HttpStatus.NOT_FOUND);
    }

    List<ScoreDTO> scoresDTO = scoreService.marshallIntoDTO(scores);

    return resFact.build("scores", scoresDTO, HttpStatus.OK);
  }

  @PostMapping("/")
  public ResponseEntity<?> postScore(@RequestBody @Valid ScoreDTO scoreDTO) {
    logger.info("In postScore() in ScoreController.");

    ScoreDTO savedScore = scoreService.save(scoreDTO);

    return resFact.build("score", savedScore, HttpStatus.CREATED);
  }

  // TODO: Auth
  @DeleteMapping("/")
  public ResponseEntity<?> removeScores() {
    logger.info("In removeScores() in ScoreController.");

    scoreService.deleteAll();

    return ResponseEntity.status(HttpStatus.OK).build();
  }

  // TODO: Implement
  @DeleteMapping("/{nickname}")
  public ResponseEntity<?> deleteScoresByNickname(
      @PathVariable("nickname") String nickname) {
    logger.info("In deleteScoresByNickname() in ScoreController.");
    logger.info("Removing high scores with nickname: " + nickname + ".");

    throw new NotYetImplementedException();
  }
}
