package tn.esprit.mindfull.Respository;

import tn.esprit.mindfull.entity.UserActivity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface UserActivityRepository extends JpaRepository<UserActivity, Long> {
    List<UserActivity> findByUserId(Long userId);
}