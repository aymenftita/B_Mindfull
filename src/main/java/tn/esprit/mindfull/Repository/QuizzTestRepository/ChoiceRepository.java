package tn.esprit.mindfull.Repository.QuizzTestRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.mindfull.entity.Quizz_Test_Game.Choice;

@Repository
public interface ChoiceRepository extends JpaRepository<Choice, Long> {
    
}
