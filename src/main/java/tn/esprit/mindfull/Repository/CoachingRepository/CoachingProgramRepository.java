package tn.esprit.mindfull.Repository.CoachingRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tn.esprit.mindfull.entity.Coaching.CoachingProgram;

import java.util.Date;
import java.util.List;

public interface CoachingProgramRepository  extends JpaRepository<CoachingProgram, Long>
{
    List<CoachingProgram> findByStartDateBetween(Date start, Date end);


        @Query("SELECT COUNT(p) FROM CoachingProgram p WHERE p.user.userId = :coachId")
        long countByCoachId(@Param("coachId") Long coachId);

        @Query("SELECT p.title, p.participants FROM CoachingProgram p")
        List<Object[]> getParticipantsByProgram();

        @Query("SELECT p.title, size(p.contents) FROM CoachingProgram p")
        List<Object[]> getContentCountByProgram();
        // Rechercher les programmes par mot-clé (dans le titre ou la description)
         @Query("SELECT p FROM CoachingProgram p WHERE LOWER(p.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(p.description) LIKE LOWER(CONCAT('%', :keyword, '%'))")
         List<CoachingProgram> searchPrograms(@Param("keyword") String keyword);




}
