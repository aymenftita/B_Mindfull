package tn.esprit.mindfull.Respository.QuizzTestRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.mindfull.entity.Quizz_Test_Game.Response;

@Repository
public interface ResponseRepository extends JpaRepository<Response, Long> {
}
