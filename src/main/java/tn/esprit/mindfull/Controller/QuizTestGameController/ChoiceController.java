package tn.esprit.mindfull.Controller.QuizTestGameController;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tn.esprit.mindfull.Service.QuizzTestService.ChoiceService;
import tn.esprit.mindfull.entity.Quizz_Test_Game.Choice;

import java.util.List;
import java.util.Optional;

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

    @GetMapping("/{id}")
    public Optional<Choice> getChoiceById(@PathVariable int id){
        return choiceService.getChoiceById(id);
    }

    @PutMapping("/{id}")
    public void updateChoice(@RequestBody Choice choice,@PathVariable int id) {
        choiceService.updateChoice(choice);
    }
}
