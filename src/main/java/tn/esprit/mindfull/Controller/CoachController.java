package tn.esprit.mindfull.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*; // <- Important pour @RestController
import tn.esprit.mindfull.Respository.CoachingProgramRepository;
import tn.esprit.mindfull.Service.CoachingProgramServiceImpl;

import java.util.HashMap;
import java.util.Map;

@RestController // ✅ Corrigé ici
@RequestMapping("/stats") // ✅ Ajouté ici pour le chemin complet
public class CoachController {

    @Autowired
    private CoachingProgramServiceImpl coachingProgramServiceImpl;
    private final CoachingProgramRepository repository;

    public CoachController(CoachingProgramRepository repository) {
        this.repository = repository;
    }

    // Méthode pour calculer le nombre de programmes d'un coach spécifique
    @GetMapping("/coach-of-the-month")
    public ResponseEntity<?> getCoachOfTheMonth() {
        Long coachId = coachingProgramServiceImpl.getCoachWithMostPrograms();
        Long programCount = repository.countByCoachId(coachId);

        Map<String, Object> response = new HashMap<>();
        response.put("coachId", coachId);
        response.put("programCount", programCount);

        return ResponseEntity.ok(response);
    }

}
