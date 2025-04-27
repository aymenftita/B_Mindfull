package tn.esprit.mindfull.Respository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tn.esprit.mindfull.entity.Evaluation;
import java.util.List;

public interface EvaluationRepository extends JpaRepository<Evaluation, Long> {
    List<Evaluation> findByProgram_ProgramId(Long programId);
    @Query("SELECT e FROM Evaluation e WHERE e.patient.userId = :userId")
    List<Evaluation> findByPatientId(@Param("userId") Long userId);
}