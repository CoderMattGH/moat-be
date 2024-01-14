package com.moat.controller;

import com.moat.highscores.HighScores;
import com.moat.entity.Score;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
public class MainController {
    private final static Logger logger = LoggerFactory.getLogger(MainController.class);

    private final HighScores highScores;

    public MainController(HighScores highScores) {
        logger.info("Constructing MainController.");

        this.highScores = highScores;
    }

    /**
     * Returns the leaderboard as a sorted array.
     * Returns 'null' if there are no scores in the database.
     * @return An array of Score objects.
     */
    @GetMapping("/get-leaderboard/")
    public Score[] getLeaderBoard() {
        logger.info("Executing getLeaderBoard() method.");

        Score[] leaderboard = this.highScores.getTopTenSortedScores();

        return leaderboard;
    }

    /**
     * Receives a Score object, and checks to see if the player score
     * is big enough to be entered into the leaderboard.
     *
     * @param score A Score object.
     * @return Returns true if it is a new high score.  False otherwise.
     */
    @PostMapping("/send-score/")
    public boolean sendScore(@RequestBody Score score) {
        boolean result = highScores.checkAndSaveIfTopTenScore(score);

        return result;
    }
}
