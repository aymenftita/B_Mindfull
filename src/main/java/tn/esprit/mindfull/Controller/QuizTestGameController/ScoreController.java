// ScoreController.java
package tn.esprit.mindfull.Controller.QuizTestGameController;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.mindfull.entity.Quizz_Test_Game.Score;
import tn.esprit.mindfull.Service.QuizzTestService.ScoreService;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/scores")
@RequiredArgsConstructor
@CrossOrigin
public class ScoreController {
    private final ScoreService scoreService;

    @PostMapping
  /*  public Score saveScore(@RequestBody Score score) {
        System.out.println("score***"+score);
        return scoreService.saveScore(score);
    }*/
    public ResponseEntity<Void> saveScore(@RequestBody Score score) {
        scoreService.saveScore(score);
        return ResponseEntity.ok().build(); // no serialization issues
    }


    @GetMapping
    public List<Score> getAllScores() {
        return scoreService.getAllScores();
    }

    @GetMapping("/user/{userId}")
    public List<Score> getScoresByUser(@PathVariable Long userId) {
        return scoreService.getScoresByUserId(userId);
    }

    @GetMapping("/date-range")
    public List<Score> getScoresByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date end) {
        return scoreService.getScoresByDateRange(start, end);
    }

}