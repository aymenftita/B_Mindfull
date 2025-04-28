package tn.esprit.mindfull.Respository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.mindfull.entity.Goal;
import java.util.List;

public interface GoalRepository extends JpaRepository<Goal, Long> {
    List<Goal> findByAppUserId(Long userId);
    List<Goal> findByAppUserIdAndCompleted(Long userId, boolean completed);
}