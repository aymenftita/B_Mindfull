package tn.esprit.mindfull.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import tn.esprit.mindfull.Respository.CoachingProgramRepository;
import tn.esprit.mindfull.Respository.ProgramProgressRepository;
import tn.esprit.mindfull.Respository.UserRepository;
import tn.esprit.mindfull.Service.ProgramProgressService;
import tn.esprit.mindfull.entity.CoachingProgram;
import tn.esprit.mindfull.entity.ProgramProgress;
import tn.esprit.mindfull.entity.User;

import java.util.List;

@RestController
@RequestMapping("/api/progress")
public class ProgramProgressController {

    @Autowired
    private ProgramProgressService progressService;
    @Autowired
    private ProgramProgressRepository programProgressRepository;
    @Autowired
    private CoachingProgramRepository coachingProgramRepository;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/mark-as-viewed")
    public ResponseEntity<String> markAsViewed(@RequestParam Long userId, @RequestParam Long programId) {
        progressService.markAsViewed(userId, programId);
        return ResponseEntity.ok("Program marked as viewed.");
    }

    // Afficher la progression de tous les patients pour un programme donné
    @GetMapping("/{programId}")
    public List<ProgramProgress> getProgress(@PathVariable Long programId) {
        return progressService.getProgressByProgram(programId);
    }


    @GetMapping("/progress")
    public List<ProgramProgress> getAllProgress() {
        return programProgressRepository.findAll();
    }

    // Ajouter une progression de programme
    @PostMapping
    public ProgramProgress addProgramProgress(@RequestBody ProgramProgress progress) {
        // Trouver le coaching program et l'utilisateur à partir des IDs
        CoachingProgram program = coachingProgramRepository.findById(progress.getProgram().getId())
                .orElseThrow(() -> new IllegalArgumentException("Program not found"));
        User user = userRepository.findById(progress.getUser().getId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // Mettre à jour les objets ProgramProgress avec les entités trouvées
        progress.setProgram(program);
        progress.setUser(user);

        // Sauvegarder et retourner l'objet
        return programProgressRepository.save(progress);
    }

}