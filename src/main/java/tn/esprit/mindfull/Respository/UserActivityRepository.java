package tn.esprit.mindfull.Respository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.mindfull.entity.UserActivity;

import java.time.LocalDateTime;
import java.util.List;

public interface UserActivityRepository extends JpaRepository<UserActivity, Long> {

    List<UserActivity> findByUserId(Long userId);

    List<UserActivity> findByUserIdAndTimestampBetween(Long userId, LocalDateTime start, LocalDateTime end);
}