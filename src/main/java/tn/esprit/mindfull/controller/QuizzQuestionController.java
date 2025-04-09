// QuizzQuestionController.java
package tn.esprit.mindfull.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.mindfull.entity.Quizz_Test_Game.Choice;
import tn.esprit.mindfull.entity.Quizz_Test_Game.Quizz;
import tn.esprit.mindfull.entity.Quizz_Test_Game.QuizzQuestion;
import tn.esprit.mindfull.Service.QuizzTestService.QuizzQuestionService;

import java.util.List;

@RestController
@RequestMapping("/api/questions")
@RequiredArgsConstructor
@CrossOrigin
public class QuizzQuestionController {
    private final QuizzQuestionService quizzQuestionService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createQuestion(@RequestBody QuizzQuestion questionDTO) {
        try {
            QuizzQuestion createdQuestion = quizzQuestionService.createQuestion(questionDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdQuestion);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error creating question: " + e.getMessage());
        }
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