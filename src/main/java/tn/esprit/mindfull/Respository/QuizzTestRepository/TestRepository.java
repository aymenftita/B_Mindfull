package tn.esprit.mindfull.Respository.QuizzTestRepository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.mindfull.entity.Quizz_Test_Game.Test;

@Repository
public interface TestRepository extends JpaRepository<Test, Long> {

}
