package tn.esprit.mindfull.Respository;

import tn.esprit.mindfull.entity.CoachingProgram;
import tn.esprit.mindfull.entity.ProgramProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.mindfull.entity.User;

import java.util.List;
import java.util.Optional;

public interface ProgramProgressRepository extends JpaRepository<ProgramProgress, Long> {
    // Cette méthode va générer automatiquement l'implémentation par Spring Data JPA
    List<ProgramProgress> findByProgramId(Long programId);

    // Cette méthode génère aussi l'implémentation automatiquement
    Optional<ProgramProgress> findByUserIdAndProgramId(Long userId, Long programId);
    Optional<ProgramProgress> findByUserAndProgram(User user, CoachingProgram program);
}
