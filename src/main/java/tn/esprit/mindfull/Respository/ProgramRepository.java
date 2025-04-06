package tn.esprit.mindfull.Respository;

import tn.esprit.mindfull.entity.Programs;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProgramRepository extends JpaRepository<Programs, Long> {
    List<Programs> findByMoodTarget(String moodTarget);
}