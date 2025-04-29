package tn.esprit.mindfull.Repository.GroupRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.mindfull.entity.Group.Achievement;
import java.util.List;

public interface AchievementRepository extends JpaRepository<Achievement, Long> {
    List<Achievement> findByUserId(Long userId);
}