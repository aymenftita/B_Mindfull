package tn.esprit.mindfull.Repository.QuizzTestRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.mindfull.entity.Quizz_Test_Game.Score;

import java.util.Date;
import java.util.List;

@Repository
public interface ScoreRepository extends JpaRepository<Score,Long> {
    List<Score> findByUserUserId(Long userId);
    List<Score> findByDateBetween(Date startDate, Date endDate);
}
