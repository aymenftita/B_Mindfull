package tn.esprit.mindfull.Respository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tn.esprit.mindfull.entity.Coach;

import java.util.List;
import java.util.Optional;

@Repository
public interface CoachRepository extends JpaRepository<Coach, Long> {
    Optional<Coach> findById(Long id);
    @Query("SELECT c FROM Coach c JOIN c.coachingPrograms cp GROUP BY c.coach_id ORDER BY COUNT(cp) DESC")
    List<Coach> findCoachWithMostPrograms();
}