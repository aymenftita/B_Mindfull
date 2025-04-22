package tn.esprit.mindfull.Respository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import tn.esprit.mindfull.entity.CoachingProgram;

import java.util.Date;
import java.util.List;

public interface CoachingProgramRepository  extends JpaRepository<CoachingProgram, Long>
{
    List<CoachingProgram> findByStartDateBetween(Date start, Date end);

    @Query("SELECT cp.coach.user_id FROM CoachingProgram cp GROUP BY cp.coach.user_id ORDER BY COUNT(cp.programId) DESC LIMIT 1")
    Long findCoachIdWithMostPrograms();



    Long countByCoachId(Long coachId);
}
