package tn.esprit.mindfull.Respository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.mindfull.entity.CoachingProgram;

import java.util.Date;
import java.util.List;

public interface CoachingProgramRepository  extends JpaRepository<CoachingProgram, Long>
{
    List<CoachingProgram> findByStartDateBetween(Date start, Date end);

}
