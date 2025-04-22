package tn.esprit.mindfull.Service.QuizzTestService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.mindfull.Respository.QuizzTestRepository.QuizzRepository;
import tn.esprit.mindfull.entity.Quizz_Test_Game.Quizz;
import tn.esprit.mindfull.entity.Quizz_Test_Game.QuizzQuestion;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QuizzService {
    private final QuizzRepository quizzRepository;

    public Quizz createQuizz(Quizz quizz) {
        return quizzRepository.save(quizz);
    }

    public Quizz getQuizzById(Long id) {
        return quizzRepository.findById(id).orElse(null);
    }

    public List<Quizz> getAllQuizzes() {
        return quizzRepository.findAll();
    }

    /*public Quizz updateQuizz(Long id, Quizz quizz) {
        if (quizzRepository.existsById(id)) {
            quizz.setQuizz_id(id);
            return quizzRepository.save(quizz);
        }
        return null;
    }*/

    public Quizz updateQuizz(Long id, Quizz quizzUpdates) {
        return quizzRepository.findById(id)
                .map(existingQuizz -> {

                    existingQuizz.setTitle(quizzUpdates.getTitle());
                    existingQuizz.setImage(quizzUpdates.getImage());
                    existingQuizz.setDescription(quizzUpdates.getDescription());

                    return quizzRepository.save(existingQuizz);
                })
                .orElse(null);
    }

    public void deleteQuizz(Long id) {
        quizzRepository.deleteById(id);
    }

    public Quizz addQuestionToQuizz(Long quizzId, QuizzQuestion question) {
        return quizzRepository.findById(quizzId).map(quizz -> {
            question.setQuizz(quizz);
            quizz.getQuestions().add(question);
            return quizzRepository.save(quizz);
        }).orElse(null);
    }
}