package tn.esprit.mindfull.Respository.QuizzTestRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.mindfull.entity.Quizz_Test_Game.QuizzQuestion;

@Repository
public interface QuizzQuestionRepository extends JpaRepository<QuizzQuestion, Long> {
}
