package tn.esprit.mindfull.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tn.esprit.mindfull.Service.QuizzTestService.ChoiceService;
import tn.esprit.mindfull.Service.QuizzTestService.ScoreService;
import tn.esprit.mindfull.entity.Quizz_Test_Game.Choice;
import tn.esprit.mindfull.entity.Quizz_Test_Game.Score;

import java.util.List;

@RestController
@RequestMapping("/api/choices")
@RequiredArgsConstructor
@CrossOrigin
public class ChoiceController {
    private final ChoiceService choiceService;

    @PostMapping
    public Choice saveChoice(@RequestBody Choice choice) {
        return choiceService.saveChoice(choice);
    }

    @GetMapping
    public List<Choice> getAllChoices() {
        return choiceService.getAllChoices();
    }

    /*@GetMapping("/user/{userId}")
    public List<Choice> getScoresByUser(@PathVariable Long userId) {
        return choiceService.getScoresByUserId(userId);
    }*/

    @DeleteMapping("/{id}")
    public void deleteChoice(@PathVariable long id) {choiceService.deleteChoice(id); }


}
