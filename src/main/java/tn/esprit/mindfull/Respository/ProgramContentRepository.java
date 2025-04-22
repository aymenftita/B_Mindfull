package tn.esprit.mindfull.Respository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.mindfull.entity.CoachingProgram;
import tn.esprit.mindfull.entity.ProgramContent;
import tn.esprit.mindfull.entity.User;

import java.util.List;

public interface ProgramContentRepository extends JpaRepository<ProgramContent, Long>
{
    List<ProgramContent> findByProgramAndUser(CoachingProgram program, User user);
    int countByProgramAndUsers(CoachingProgram program, User user);
    int countByProgramAndUsersAndCompletedTrue(CoachingProgram program, User user);
}
