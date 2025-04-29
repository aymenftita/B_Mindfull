package tn.esprit.mindfull.Repository.GroupRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.mindfull.entity.Group.Goal;
import java.util.List;

public interface GoalRepository extends JpaRepository<Goal, Long> {
    List<Goal> findByUserId(Long userId);
    List<Goal> findByUserIdAndCompleted(Long userId, boolean completed);
}