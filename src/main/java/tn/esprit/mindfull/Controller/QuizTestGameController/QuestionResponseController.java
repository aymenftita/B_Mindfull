package tn.esprit.mindfull.Controller.QuizTestGameController;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tn.esprit.mindfull.Service.QuizzTestService.QuestionResponseService;
import tn.esprit.mindfull.entity.Quizz_Test_Game.QuestionResponse;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/question_response")
@RequiredArgsConstructor
public class QuestionResponseController {
    private final QuestionResponseService questionResponseService;

    @GetMapping
    public List<QuestionResponse> getAllQuestionResponse(){
        return questionResponseService.getAllQuestionResponse();
    }

    @GetMapping("/{id}")
    public Optional<QuestionResponse> getQuestionResponse(@PathVariable int id) {
        return questionResponseService.getQuestionResponse(id);
    }

    @PostMapping
    public QuestionResponse addQuestionResponse(@RequestBody QuestionResponse questionResponse) {
        return questionResponseService.addQuestionResponse(questionResponse);
    }

    @DeleteMapping("/{id}")
    public void deleteQuestionResponse(@PathVariable int id) {
        questionResponseService.deleteQuestionResponse(id);
    }

    @PutMapping("/{id}")
    public Optional<QuestionResponse> updateQuestionResponse(@PathVariable int id, @RequestBody QuestionResponse questionResponse) {
        return questionResponseService.updateQuestionResponse(id, questionResponse);
    }
}
