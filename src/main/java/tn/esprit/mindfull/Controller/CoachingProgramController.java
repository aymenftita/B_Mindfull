package tn.esprit.mindfull.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.mindfull.Respository.CoachingProgramRepository;
import tn.esprit.mindfull.entity.CoachingProgram;
import tn.esprit.mindfull.Service.ICoachingProgramService;

import java.util.List;

@RestController
@RequestMapping("/coaching-programs")

public class CoachingProgramController {
    @Autowired
    private ICoachingProgramService iCoachingProgramService;
    private  final CoachingProgramRepository repository;
    public CoachingProgramController(CoachingProgramRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/coachingPrograms")
    public List<CoachingProgram> getAllPrograms() {

        return iCoachingProgramService.getAllPrograms();
    }

    @GetMapping("/coachingProgram/{id}")
    public CoachingProgram getProgrammeById(@PathVariable Long id) {

        return iCoachingProgramService.getProgramById(id);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CoachingProgram> createProgram(
            @RequestBody CoachingProgram program // Reçoit directement l'entité
    ) {
        CoachingProgram savedProgram = repository.save(program);
        return ResponseEntity.ok(savedProgram);
    }

    @PutMapping("/coachingProgram/{id}")
    public ResponseEntity<?> updateProgram(
            @PathVariable Long id,
            @RequestBody CoachingProgram programDetails) {

        return repository.findById(id)
                .map(program -> {
                    program.setTitle(programDetails.getTitle());
                    program.setDescription(programDetails.getDescription());
                    program.setParticipants(programDetails.getParticipants());


                    // autres mises à jour...
                    CoachingProgram updated = repository.save(program);
                    return ResponseEntity.ok(updated);
                })
                .orElse(ResponseEntity.notFound().build());
    }
    @DeleteMapping("/coachingProgram/{id}")
    public void deleteProgram (@PathVariable Long id) {

        iCoachingProgramService.deleteProgram(id);
    }

}
