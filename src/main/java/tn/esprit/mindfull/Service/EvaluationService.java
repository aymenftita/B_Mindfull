package tn.esprit.mindfull.Service;


import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.mindfull.entity.*;
import tn.esprit.mindfull.Respository.*;

import java.util.List;

@Service
@AllArgsConstructor
public class EvaluationService {
    private final EvaluationRepository evaluationRepository;
    private final UserRepository userRepository;
    private final CoachingProgramRepository programRepository;

    public Evaluation addEvaluation(Evaluation evaluation, Long userId, Long programId) {
        User patient = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Patient not found"));
        CoachingProgram program = programRepository.findById(programId)
                .orElseThrow(() -> new RuntimeException("Program not found"));

        evaluation.setPatient(patient);
        evaluation.setProgram(program);
        return evaluationRepository.save(evaluation);
    }

    public double getAverageRating(Long programId) {
        return evaluationRepository.findByProgram_ProgramId(programId)
                .stream()
                .mapToInt(Evaluation::getRating)
                .average()
                .orElse(0.0);
    }

    public List<Evaluation> getProgramEvaluations(Long programId) {
        return evaluationRepository.findByProgram_ProgramId(programId);
    }
}
