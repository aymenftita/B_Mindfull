package tn.esprit.mindfull.Controller;


import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.mindfull.entity.Evaluation;
import tn.esprit.mindfull.Service.EvaluationService;
import java.util.List;

@RestController
@RequestMapping("/api/evaluations")
@AllArgsConstructor
public class EvaluationController {
    private final EvaluationService evaluationService;

    @PostMapping("/{patientId}/{programId}")
    public ResponseEntity<Evaluation> addEvaluation(
            @RequestBody Evaluation evaluation,
            @PathVariable Long userId,
            @PathVariable Long programId) {
        return ResponseEntity.ok(
                evaluationService.addEvaluation(evaluation, userId, programId)
        );
    }

    @GetMapping("/average/{programId}")
    public ResponseEntity<Double> getAverageRating(
            @PathVariable Long programId) {
        return ResponseEntity.ok(
                evaluationService.getAverageRating(programId)
        );
    }

    @GetMapping("/program/{programId}")
    public ResponseEntity<List<Evaluation>> getProgramEvaluations(
            @PathVariable Long programId) {
        return ResponseEntity.ok(
                evaluationService.getProgramEvaluations(programId)
        );
    }
}