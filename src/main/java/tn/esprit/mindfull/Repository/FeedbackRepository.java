package tn.esprit.mindfull.Respository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.mindfull.entity.Feedback;

import java.util.List;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
    List<Feedback> findByProgramContent_ContentId(Long contentId);

    List<Feedback> findByProgramContent_Program_ProgramId(Long programId);

    // Utilisez le nom de la relation (coach) puis id
    List<Feedback> findByCoach_Id(Long coachId);

    // Pour le patient
    List<Feedback> findByPatient_Id(Long patientId);

    // Pour le contenu de programme

}