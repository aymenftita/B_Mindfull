package tn.esprit.mindfull.Controller;

import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.mindfull.Respository.ProgramContentRepository;
import tn.esprit.mindfull.Respository.UserRepository;
import tn.esprit.mindfull.Service.EmailService;
import tn.esprit.mindfull.Service.IProgramContentService;
import tn.esprit.mindfull.Service.UserService;
import tn.esprit.mindfull.entity.ProgramContent;
import tn.esprit.mindfull.entity.Role;
import tn.esprit.mindfull.entity.User;

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



        // Récupérer l'utilisateur ayant le rôle 'PATIENT' (vous pouvez filtrer par ID ou d'autres critères si nécessaire)
        List<User> patients = userService.findUserByRole("PATIENT");

        if (patients.isEmpty()) {
            User patient = patients.get(0);
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
    @PutMapping("/programcontent/{contentId}")
    public ProgramContent updateContent(@PathVariable Long contentId, @RequestBody ProgramContent programContent) {
        return iProgramContentService.updateContent(contentId, programContent);
    }
    @DeleteMapping("/programcontent/{id}")
    public void deleteContent(@PathVariable Long id) {

        iProgramContentService.deleteContent(id);
    }
    // API pour ajouter un patient à un programme et envoyer un email
   /* @PostMapping("/add")
    public String addPatientToProgram(@RequestParam Long contentId) {
        try {
            iProgramContentService.addProgramContent(contentId);
            return "Patient ajouté au programme et email envoyé.";
        } catch (MessagingException e) {
            return "Erreur lors de l'envoi de l'email : " + e.getMessage();
        }
    }*/
}
