package tn.esprit.mindfull.Controller.CoachingController;

import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.mindfull.Repository.CoachingRepository.CoachingProgramRepository;
import tn.esprit.mindfull.Repository.CoachingRepository.ProgramContentRepository;
import tn.esprit.mindfull.Service.CoachingService.EmailService;
import tn.esprit.mindfull.Service.CoachingService.IProgramContentService;
import tn.esprit.mindfull.entity.Coaching.ProgramContent;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/program-contents")
public class ProgramContentController {

    @Autowired
    private IProgramContentService iProgramContentService;
    @Autowired
    private   ProgramContentRepository programContentRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EmailService emailService; // Injecting the EmailService
    @Autowired
    private UserService userService; // Injecting the UserService
    @Autowired
    private CoachingProgramRepository coachingProgramRepository;
    public ProgramContentController(ProgramContentRepository programContentRepository) {
        this.programContentRepository = programContentRepository;
    }

    @GetMapping("/programcontents")
    public List<ProgramContent> getAllContent() {

        return iProgramContentService.getAllContent();
    }

    @GetMapping("/{id}")
    public ProgramContent getContentById(@PathVariable Long id) {

        return iProgramContentService.getContentById(id);
    }

    @PostMapping("/programcontent")
    public ResponseEntity<ProgramContent> create(@RequestBody ProgramContent programContent) {
        ProgramContent saved = iProgramContentService.saveContent(programContent);
        System.out.println("Content ID généré : " + saved.getContentId());

        // Récupérer l'utilisateur ayant le rôle 'PATIENT'
        List<User> patients = userService.findUserByRole("PATIENT");

        if (!patients.isEmpty()) { // Vérifier que la liste n'est pas vide
            User patient = patients.get(0); // Maintenant c'est sûr qu'il y a au moins un patient
            String patientEmail = patient.getEmail(); // Récupérer l'email du patient
            String subject = "Nouveau contenu ajouté";
            String content = "Bonjour, un nouveau contenu a été ajouté à votre programme. Veuillez vérifier les détails.";

            try {
                // Envoi de l'email au patient
                emailService.sendEmail(patientEmail, subject, content);
                System.out.println("Email envoyé à : " + patientEmail);
            } catch (MessagingException e) {
                System.out.println("Erreur d'envoi de l'email : " + e.getMessage());
            }
        } else {
            System.out.println("Aucun patient trouvé avec ce rôle.");
        }

        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }



    // Pour mettre à jour un contenu
    // Pour mettre à jour un contenu
    @PutMapping("/programcontent/{contentId}")
    public ProgramContent updateContent(@PathVariable Long contentId, @RequestBody ProgramContent programContent) {
        return iProgramContentService.updateContent(contentId, programContent);
    }

    @DeleteMapping("/programcontent/{id}")
    public void deleteContent(@PathVariable Long id) {

        iProgramContentService.deleteContent(id);
    }
    @GetMapping("/{contentId}/users")
    public ResponseEntity<Object> getUsersByContent(@PathVariable Long contentId) {
        Optional<ProgramContent> content = programContentRepository.findById(contentId);
        return content.map(value -> ResponseEntity.ok(value.getUsers()))
                .orElse(ResponseEntity.notFound().build());
    }
    @GetMapping  // Change the URL to avoid the ambiguity
    public Page<ProgramContent> getProgramContents(@RequestParam int page, @RequestParam int size) {
        Pageable pageable = PageRequest.of(page, size);
        return programContentRepository.findAll(pageable);
    }
    @GetMapping("/search")
    public List<ProgramContent> searchContents(@RequestParam String keyword) {
        return programContentRepository.searchContents(keyword);
    }



}
