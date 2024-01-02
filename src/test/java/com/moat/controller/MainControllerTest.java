package com.moat.controller;

import com.moat.entity.Score;
import com.moat.highscores.HighScores;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MainControllerTest {
    private final static Logger logger = LoggerFactory.getLogger(MainControllerTest.class);

    private final Score[] leaderboard = new Score[5];

    public MainControllerTest() {
        logger.info("Constructing MainControllerTest.");
    }

    @BeforeEach
    public void initLeaderBoard() {
        Score score1 = new Score(100, "mattd");
        leaderboard[4] = score1;

        Score score2 = new Score(3000, "bobby");
        leaderboard[3] = score2;

        Score score3 = new Score(5000, "timmy");
        leaderboard[2] = score3;

        Score score4 = new Score(10000, "tommy");
        leaderboard[1] = score4;

        Score score5 = new Score(120000, "alan5");
        leaderboard[0] = score5;
    }

    @Test
    public void testGetLeaderBoard() {
        HighScores highScores = mock(HighScores.class);
        when(highScores.getTopTenSortedScores()).thenReturn(leaderboard);

        MainController mainController = new MainController(highScores);

        assertEquals(5, mainController.getLeaderBoard().length);
        assertEquals("ALAN5", mainController.getLeaderBoard()[0].getNickname());
    }

    @Test
    public void testSendScore() {
        HighScores highScores = mock(HighScores.class);
        when(highScores.checkAndSaveIfTopTenScore(any())).thenReturn(true);

        MainController mainController = new MainController(highScores);

        Score score = new Score(50000, "mattd");

        boolean result = mainController.sendScore(score);

        assertTrue(result);
    }
}