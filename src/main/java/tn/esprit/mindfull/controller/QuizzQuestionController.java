// QuizzQuestionController.java
package tn.esprit.mindfull.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tn.esprit.mindfull.entity.Quizz_Test_Game.Choice;
import tn.esprit.mindfull.entity.Quizz_Test_Game.QuizzQuestion;
import tn.esprit.mindfull.Service.QuizzTestService.QuizzQuestionService;

import java.util.List;

@RestController
@RequestMapping("/api/questions")
@RequiredArgsConstructor
public class QuizzQuestionController {
    private final QuizzQuestionService quizzQuestionService;

    @PostMapping
    public QuizzQuestion createQuestion(@RequestBody QuizzQuestion question) {
        return quizzQuestionService.createQuestion(question);
    }

    @GetMapping("/{id}")
    public QuizzQuestion getQuestionById(@PathVariable Long id) {
        return quizzQuestionService.getQuestionById(id);
    }

    @GetMapping
    public List<QuizzQuestion> getAllQuestions() {
        return quizzQuestionService.getAllQuestions();
    }

    @PutMapping("/{id}")
    public QuizzQuestion updateQuestion(@PathVariable Long id, @RequestBody QuizzQuestion question) {
        return quizzQuestionService.updateQuestion(id, question);
    }

    @DeleteMapping("/{id}")
    public void deleteQuestion(@PathVariable Long id) {
        quizzQuestionService.deleteQuestion(id);
    }

    @PostMapping("/{questionId}/choices")
    public QuizzQuestion addChoiceToQuestion(@PathVariable Long questionId, @RequestBody Choice choice) {
        return quizzQuestionService.addChoiceToQuestion(questionId, choice);
    }
}