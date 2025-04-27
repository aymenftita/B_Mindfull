package tn.esprit.mindfull.Controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import tn.esprit.mindfull.Respository.FeedbackRepository;
import tn.esprit.mindfull.Service.AIService;
import tn.esprit.mindfull.entity.Feedback;
import tn.esprit.mindfull.Service.FeedbackService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/feedback")
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:4200") // 🔥 Pour autoriser Angular localhost:4200
public class FeedbackController {

    @Autowired
    private FeedbackService feedbackService;
    private final List<String> notifications = new ArrayList<>(); // 🛎️ Liste de notifications

    @Autowired
    private AIService aiService;
    private FeedbackRepository feedbackRepository;

  /*  @PostMapping
    public ResponseEntity<String> receiveFeedback(@RequestBody Map<String, Object> feedback) {
        try {
            // 🔵 Lire rating et comment envoyés depuis Angular
            String ratingStr = feedback.get("rating").toString(); // ✔️ pas de cast direct
            Integer rating = Integer.parseInt(ratingStr); // ✔️ convertir
            String comment = (String) feedback.get("comment");

            // 🔵 Juste afficher en console pour vérifier
            System.out.println("Rating reçu: " + rating);
            System.out.println("Commentaire reçu: " + comment);
            // 🛎️ Ajouter une notification pour le coach
            notifications.add("Nouveau feedback reçu: " + comment);

            // 🔵 Envoyer une réponse au frontend
            return ResponseEntity.ok("Feedback reçu avec succès !");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Erreur lors de la réception du feedback");

        }
    }*/
    // Endpoint pour récupérer les notifications
    @GetMapping("/notifications")
    public ResponseEntity<List<String>> getNotifications() {
        List<String> notifications = new ArrayList<>();
        notifications.add("Un nouveau feedback a été reçu !");
        notifications.add("Un autre feedback a été ajouté !");
        return ResponseEntity.ok(notifications);
    }

    @PostMapping("/{patientId}/{contentId}")
    public ResponseEntity<Feedback> addFeedback(
            @RequestBody Feedback feedback,
            @PathVariable Long patientId,
            @PathVariable Long contentId
    ) {
        Feedback savedFeedback = feedbackService.addFeedback(feedback, patientId, contentId);
        // 🛎️ Ajouter une notification pour ce feedback aussi
        notifications.add("Nouveau feedback reçu pour le contenu ID " + contentId);
        return ResponseEntity.ok(savedFeedback);
    }

    @GetMapping("/average/{programId}")
    public ResponseEntity<Double> getAverageRating(@PathVariable Long programId) {
        double average = feedbackService.getAverageRatingForProgram(programId);
        return ResponseEntity.ok(average);
    }
    // Méthode pour analyser un feedback
    @PutMapping("/diagnose/{feedbackId}")
    public Feedback diagnoseFeedback(@PathVariable Long feedbackId) {
        // Récupérer le feedback depuis la base de données
        Feedback feedback = feedbackRepository.findById(feedbackId).orElseThrow(() -> new RuntimeException("Feedback introuvable"));

        // Obtenir le diagnostic de l'API AI
        String diagnostic = aiService.getDiagnosis(feedback.getComment());

        // Définir le diagnostic dans l'objet feedback et le sauvegarder
        feedback.setDiagnostic(diagnostic);
        return feedbackRepository.save(feedback);
    }
    // Endpoint pour récupérer tous les feedbacks
    @GetMapping("/all")
    public ResponseEntity<List<Feedback>> getAllFeedbacks() {
        List<Feedback> feedbacks = feedbackService.getAllFeedbacks();  // Assurez-vous que la méthode existe dans le service
        return ResponseEntity.ok(feedbacks);
    }

}