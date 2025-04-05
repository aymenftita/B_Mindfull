// ScoreController.java
package tn.esprit.mindfull.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tn.esprit.mindfull.entity.Quizz_Test_Game.Score;
import tn.esprit.mindfull.Service.QuizzTestService.ScoreService;

import java.util.List;

@RestController
@RequestMapping("/api/scores")
@RequiredArgsConstructor
public class ScoreController {
    private final ScoreService scoreService;

    @PostMapping
    public Score saveScore(@RequestBody Score score) {
        return scoreService.saveScore(score);
    }

    @GetMapping
    public List<Score> getAllScores() {
        return scoreService.getAllScores();
    }

    @GetMapping("/user/{userId}")
    public List<Score> getScoresByUser(@PathVariable Long userId) {
        return scoreService.getScoresByUserId(userId);
    }

}