package tn.esprit.mindfull.Service.QuizzTestService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.mindfull.Repository.QuizzTestRepository.QuizzQuestionRepository;
import tn.esprit.mindfull.entity.Quizz_Test_Game.Choice;
import tn.esprit.mindfull.entity.Quizz_Test_Game.QuizzQuestion;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QuizzQuestionService {
    private final QuizzQuestionRepository quizzQuestionRepository;

    public QuizzQuestion createQuestion(QuizzQuestion question) {
        return quizzQuestionRepository.save(question);
    }

    public QuizzQuestion getQuestionById(Long id) {
        return quizzQuestionRepository.findById(id).orElse(null);
    }

    public List<QuizzQuestion> getAllQuestions() {
        return quizzQuestionRepository.findAll();
    }

    public QuizzQuestion updateQuestion(Long id, QuizzQuestion questionUpdates) {
        return quizzQuestionRepository.findById(id)
                .map(existingQuestion -> {

                    existingQuestion.setQuestionText(questionUpdates.getQuestionText());
                    existingQuestion.setText(questionUpdates.isText());

                    return quizzQuestionRepository.save(existingQuestion);
                })
                .orElse(null);
    }

    public void deleteQuestion(Long id) {
        quizzQuestionRepository.deleteById(id);
    }

    public QuizzQuestion addChoiceToQuestion(Long questionId, Choice choice) {
        return quizzQuestionRepository.findById(questionId).map(question -> {
            choice.setQuizzQuestion(question);
            question.getChoices().add(choice);
            return quizzQuestionRepository.save(question);
        }).orElse(null);
    }

}