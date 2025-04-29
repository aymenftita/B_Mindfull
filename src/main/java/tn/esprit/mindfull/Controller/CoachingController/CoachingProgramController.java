package tn.esprit.mindfull.Controller.CoachingController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.mindfull.Repository.CoachingRepository.CoachingProgramRepository;
import tn.esprit.mindfull.Repository.UserRepository;
import tn.esprit.mindfull.entity.Coaching.CoachingProgram;
import tn.esprit.mindfull.Service.CoachingService.ICoachingProgramService;

import java.util.List;

@RestController
@RequestMapping("/coaching-programs")

public class CoachingProgramController {
    @Autowired
    private ICoachingProgramService iCoachingProgramService;
    private  final CoachingProgramRepository repository;
    private  final UserRepository userRepository;
    public CoachingProgramController(CoachingProgramRepository repository, UserRepository userRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
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
            @RequestBody CoachingProgram program, @RequestParam Long userId // Reçoit directement l'entité
    ) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        program.setUser(user);
        CoachingProgram savedProgram = repository.save(program);
        return ResponseEntity.ok(savedProgram);
    }

    @PutMapping("/coachingProgram/{id}")
    public ResponseEntity<?> updateProgram(
            @PathVariable Long id,
            @RequestBody CoachingProgram programDetails) {

        return repository.findById(id)
                .map(program -> {

                    program.setCoachId(programDetails.getCoachId()); // ✅ ligne à ajouter
                    program.setTitle(programDetails.getTitle());
                    program.setDescription(programDetails.getDescription());
                    program.setParticipants(programDetails.getParticipants());
                    program.setStartDate(programDetails.getStartDate());
                    program.setEndDate(programDetails.getEndDate());


                    CoachingProgram updated = repository.save(program);
                    return ResponseEntity.ok(updated);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/coachingProgram/{id}")
    public void deleteProgram (@PathVariable Long id) {

        iCoachingProgramService.deleteProgram(id);
    }
    // Endpoint pour récupérer les programmes avec la pagination
    // Récupère les programmes de coaching avec pagination
    @GetMapping
    public Page<CoachingProgram> getCoachingPrograms(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return repository.findAll(pageable);
    }
    // 🔍 Recherche avec paramètre
    @GetMapping("/search")
    public List<CoachingProgram> searchPrograms(@RequestParam String keyword) {
        return repository.searchPrograms(keyword);
    }

}
