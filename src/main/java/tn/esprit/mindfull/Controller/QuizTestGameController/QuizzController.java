// QuizzController.java
package tn.esprit.mindfull.Controller.QuizTestGameController;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tn.esprit.mindfull.entity.Quizz_Test_Game.Quizz;
import tn.esprit.mindfull.entity.Quizz_Test_Game.QuizzQuestion;
import tn.esprit.mindfull.Service.QuizzTestService.QuizzService;

import java.util.List;

@RestController
@RequestMapping("/api/quizzes")
@RequiredArgsConstructor
@CrossOrigin
public class QuizzController {
    private final QuizzService quizzService;

    @PostMapping
    public Quizz createQuiz(@RequestBody Quizz quiz) {
        return quizzService.createQuizz(quiz);
    }


    @GetMapping("/{id}")
    public Quizz getQuizzById(@PathVariable Long id) {
        return quizzService.getQuizzById(id);
    }

    @GetMapping
    public List<Quizz> getAllQuizzes() {
        return quizzService.getAllQuizzes();
    }

    @PutMapping("/{id}")
    public Quizz updateQuizz(@PathVariable Long id, @RequestBody Quizz quizz) {
        return quizzService.updateQuizz(id, quizz);
    }

    @DeleteMapping("/{id}")
    public void deleteQuizz(@PathVariable Long id) {
        quizzService.deleteQuizz(id);
    }

    @PostMapping("/{quizzId}/questions")
    public Quizz addQuestionToQuizz(@PathVariable Long quizzId, @RequestBody QuizzQuestion question) {
        return quizzService.addQuestionToQuizz(quizzId, question);
    }
}