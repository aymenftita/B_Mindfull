package tn.esprit.mindfull.Repository.CoachingRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tn.esprit.mindfull.entity.Coaching.CoachingProgram;
import tn.esprit.mindfull.entity.Coaching.ProgramContent;

import java.util.List;
import java.util.Optional;

public interface ProgramContentRepository extends JpaRepository<ProgramContent, Long>
{
    List<ProgramContent> findByProgramAndUser(CoachingProgram program, User user);
    int countByProgramAndUsers(CoachingProgram program, User user);
    int countByProgramAndUsersAndCompletedTrue(CoachingProgram program, User user);

    @Query("SELECT COUNT(pc) FROM ProgramContent pc WHERE pc.program.programId = :programId")
    int countByProgramId(@Param("programId") Long programId);

    Page<ProgramContent> findByCoachingProgram_Id(Long programId, Pageable pageable);

    // Rechercher par titre ou type
    @Query("SELECT c FROM ProgramContent c WHERE LOWER(c.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(c.contentType) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<ProgramContent> searchContents(String keyword);

    Optional<ProgramContent> findByContentId(Long contentId);
}
