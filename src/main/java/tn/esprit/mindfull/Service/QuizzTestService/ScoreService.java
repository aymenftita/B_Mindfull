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

    // Create or Update a score
    public Score saveScore(Score score) {
        return scoreRepository.save(score);
    }

    // Get all scores
    public List<Score> getAllScores() {
        return scoreRepository.findAll();
    }

    // Get score by ID
    public Optional<Score> getScoreById(Long id) {
        return scoreRepository.findById(id);
    }

    // Delete a score
    public void deleteScore(Long id) {
        scoreRepository.deleteById(id);
    }

    // Get scores by user ID
    public List<Score> getScoresByUserId(Long userId) {
        return scoreRepository.findByUserId(userId);
    }

}