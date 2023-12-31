package com.moat.controller;

import com.moat.dto.NicknameDTO;
import com.moat.highscores.HighScores;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin
@RestController
@RequestMapping(value="/admin")
public class AdminController {
    private static Logger logger = LoggerFactory.getLogger(AdminController.class);

    private HighScores highScores;

    public AdminController(HighScores highScores) {
        logger.info("Construcing AdminController.");

        this.highScores = highScores;
    }

    @PostMapping("/remove-scores-with-nickname/")
    public void removeHighScoresWithNickname(@Valid @RequestBody NicknameDTO nicknameDTO) {
        logger.info("Removing high scores with nickname: " + nicknameDTO.getNickname() + ".");

        boolean result = highScores.removeScoresWithNickname(nicknameDTO.getNickname());
    }

    // TODO: Unimplemented method.
    public void removeHighScoresById(int id) {
    }

    @PostMapping("/remove-all-scores/")
    public void removeAllScores() {
        logger.info("Removing all high scores!");

        highScores.removeAllScores();
    }

    @PostMapping("/check-login/")
    public void checkLogin() {
        logger.info("Checking administrator login credentials.");
    }
}
