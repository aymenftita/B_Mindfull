package tn.esprit.mindfull.Service.QuizzTestService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.mindfull.Respository.QuizzTestRepository.ScoreRepository;
import tn.esprit.mindfull.entity.Quizz_Test_Game.Score;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ScoreService {

    private final ScoreRepository scoreRepository;


    public Score saveScore(Score score) {
        return scoreRepository.save(score);
    }


    public List<Score> getAllScores() {
        return scoreRepository.findAll();
    }


    public Optional<Score> getScoreById(Long id) {
        return scoreRepository.findById(id);
    }


    public void deleteScore(Long id) {
        scoreRepository.deleteById(id);
    }


    public List<Score> getScoresByUserId(Long userId) {
        return scoreRepository.findByUserId(userId);
    }

}