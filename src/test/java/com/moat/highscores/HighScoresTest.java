package com.moat.highscores;

import com.moat.entity.Score;
import com.moat.profanityfilter.ProfanityFilterService;
import com.moat.service.HighScores;
import com.moat.service.HighScoresImpl;
import com.moat.service.ScoreService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

public class HighScoresTest {
  private final static Logger logger = LoggerFactory.getLogger(HighScoresTest.class);

  private final List<Score> leaderboard = new ArrayList<>();

  public HighScoresTest() {
    logger.info("Constructing HighScoresTest.");
  }

  @BeforeEach
  public void initLeaderBoard() {
    Score score1 = new Score(100, "mattd");
    leaderboard.add(score1);

    Score score2 = new Score(3000, "bobby");
    leaderboard.add(score2);

    Score score3 = new Score(5000, "timmy");
    leaderboard.add(score3);

    Score score4 = new Score(10000, "tommy");
    leaderboard.add(score4);

    Score score5 = new Score(120000, "alan5");
    leaderboard.add(score5);
  }

  @Test
  public void testGetTopTenSortedScores() {
    ScoreService scoreService = Mockito.mock(ScoreService.class);
    ProfanityFilterService profanityFilterService = Mockito.mock(ProfanityFilterService.class);

    Mockito.when(scoreService.findTopTenScoresSorted()).thenReturn(leaderboard);

    HighScores highScores = new HighScoresImpl(scoreService, profanityFilterService);

    Score[] leaderboard = highScores.getTopTenSortedScores();

    assertEquals(5, leaderboard.length);
  }

  @Test
  public void testCheckAndSaveIfTopTenScore_IfValidNickname() {
    ScoreService scoreService = Mockito.mock(ScoreService.class);
    ProfanityFilterService profanityFilterService = Mockito.mock(ProfanityFilterService.class);

    Mockito.when(scoreService.findTopTenScoresSorted()).thenReturn(leaderboard);

    Mockito.when(profanityFilterService.isEnabled()).thenReturn(true);
    Mockito.when(profanityFilterService.isValid(Mockito.anyString())).thenReturn(true);

    HighScores highScores = new HighScoresImpl(scoreService, profanityFilterService);

    Score score = new Score(20000, "mattd");

    assertTrue(highScores.checkAndSaveIfTopTenScore(score));
  }

  @Test
  public void testCheckAndSaveIfTopTenScore_IfInvalidNickname() {
    ScoreService scoreService = Mockito.mock(ScoreService.class);
    ProfanityFilterService profanityFilterService = Mockito.mock(ProfanityFilterService.class);

    Mockito.when(scoreService.findTopTenScoresSorted()).thenReturn(leaderboard);

    Mockito.when(profanityFilterService.isEnabled()).thenReturn(true);
    Mockito.when(profanityFilterService.isValid(Mockito.anyString())).thenReturn(false);

    HighScores highScores = new HighScoresImpl(scoreService, profanityFilterService);

    Score score = new Score(20000, "invalid");

    assertFalse(highScores.checkAndSaveIfTopTenScore(score));
  }

  @Test
  public void testCheckAndSaveIfTopTenScore_IfProfFilterDisabled() {
    ScoreService scoreService = Mockito.mock(ScoreService.class);
    ProfanityFilterService profanityFilterService = Mockito.mock(ProfanityFilterService.class);

    Mockito.when(scoreService.findTopTenScoresSorted()).thenReturn(leaderboard);

    Mockito.when(profanityFilterService.isEnabled()).thenReturn(false);
    Mockito.when(profanityFilterService.isValid(any())).thenReturn(false);

    HighScores highScores = new HighScoresImpl(scoreService, profanityFilterService);

    Score score = new Score(20000, "invalid");

    assertTrue(highScores.checkAndSaveIfTopTenScore(score));
  }

  @Test
  public void testCheckAndSaveIfTopTenScore_IfLeaderboardIsFullAndHighScore() {
    // Add more scores to fill up the leaderboard.
    Score score1 = new Score(1002, "mattd");
    leaderboard.add(score1);

    Score score2 = new Score(30003, "bobby");
    leaderboard.add(score2);

    Score score3 = new Score(500, "timmy");
    leaderboard.add(score3);

    Score score4 = new Score(1000, "tommy");
    leaderboard.add(score4);

    Score score5 = new Score(12000, "alan5");
    leaderboard.add(score5);

    ScoreService scoreService = Mockito.mock(ScoreService.class);
    ProfanityFilterService profanityFilterService = Mockito.mock(ProfanityFilterService.class);

    Mockito.when(scoreService.findTopTenScoresSorted()).thenReturn(leaderboard);

    Mockito.when(profanityFilterService.isEnabled()).thenReturn(true);
    Mockito.when(profanityFilterService.isValid(any())).thenReturn(true);

    HighScores highScores = new HighScoresImpl(scoreService, profanityFilterService);

    Score score = new Score(9999999, "highscore");

    assertTrue(highScores.checkAndSaveIfTopTenScore(score));
  }

  @Test
  public void testCheckAndSaveIfTopTenScore_IfLeaderboardIsFullAndNotHighScore() {
    // Add more scores to fill up the leaderboard.
    Score score1 = new Score(1002, "mattd");
    leaderboard.add(score1);

    Score score2 = new Score(30003, "bobby");
    leaderboard.add(score2);

    Score score3 = new Score(500, "timmy");
    leaderboard.add(score3);

    Score score4 = new Score(1000, "tommy");
    leaderboard.add(score4);

    Score score5 = new Score(12000, "alan5");
    leaderboard.add(score5);

    ScoreService scoreService = Mockito.mock(ScoreService.class);
    ProfanityFilterService profanityFilterService = Mockito.mock(ProfanityFilterService.class);

    Mockito.when(scoreService.findTopTenScoresSorted()).thenReturn(leaderboard);

    Mockito.when(profanityFilterService.isEnabled()).thenReturn(true);
    Mockito.when(profanityFilterService.isValid(any())).thenReturn(true);

    HighScores highScores = new HighScoresImpl(scoreService, profanityFilterService);

    Score score = new Score(10, "lowscore");

    assertFalse(highScores.checkAndSaveIfTopTenScore(score));
  }

  @Test
  public void testRemoveScoresWithNickname_valid2Entries() {
    ScoreService scoreService = Mockito.mock(ScoreService.class);
    ProfanityFilterService profanityFilterService = Mockito.mock(ProfanityFilterService.class);

    String nickname = "mattd";

    List<Score> scores = new ArrayList<>();

    Score score1 = new Score(10000, nickname);
    scores.add(score1);

    Score score2 = new Score(120000, nickname);
    scores.add(score2);

    Mockito.when(scoreService.findScoresByNickname(eq(nickname))).thenReturn(scores);

    HighScores highScores = new HighScoresImpl(scoreService, profanityFilterService);

    boolean result = highScores.removeScoresWithNickname(nickname);

    assertTrue(result);
  }

  @Test
  public void testRemoveScoresWithNickname_validNoEntries() {
    ScoreService scoreService = Mockito.mock(ScoreService.class);
    ProfanityFilterService profanityFilterService = Mockito.mock(ProfanityFilterService.class);

    String nickname = "mattd";

    List<Score> scores = new ArrayList<>();

    Mockito.when(scoreService.findScoresByNickname(eq(nickname))).thenReturn(scores);

    HighScores highScores = new HighScoresImpl(scoreService, profanityFilterService);

    boolean result = highScores.removeScoresWithNickname(nickname);

    assertFalse(result);
  }

  @Test
  public void testRemoveScoresWithNickname_invalidNickname() {
    ScoreService scoreService = Mockito.mock(ScoreService.class);
    ProfanityFilterService profanityFilterService = Mockito.mock(ProfanityFilterService.class);

    HighScores highScores = new HighScoresImpl(scoreService, profanityFilterService);

    assertFalse(highScores.removeScoresWithNickname(""));
    assertFalse(highScores.removeScoresWithNickname(null));
  }
}
