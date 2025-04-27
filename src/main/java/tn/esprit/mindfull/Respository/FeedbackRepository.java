package tn.esprit.mindfull.Respository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.mindfull.entity.Feedback;

import java.util.List;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
    List<Feedback> findByProgramContent_ContentId(Long contentId);
    List<Feedback> findByProgramContent_Program_ProgramId(Long programId);
}