package tn.esprit.mindfull.Controller.CoachingController;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.mindfull.Repository.CoachingRepository.FeedbackRepository;
import tn.esprit.mindfull.Service.CoachingService.AIService;
import tn.esprit.mindfull.entity.Coaching.Feedback;
import tn.esprit.mindfull.Service.CoachingService.FeedbackService;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/feedback")
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:4200") // 🔥 Pour autoriser Angular localhost:4200
public class FeedbackController {

    @Autowired
    private FeedbackService feedbackService;
  //  @Autowired
   // private final NotificationService notificationService;

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
        // Envoyer une notification au coach
      //  String message = "Nouveau feedback reçu de " ;
      //  notificationService.createNotification(message, feedback.getCoachId(), savedFeedback.getContentId());
        return ResponseEntity.ok(savedFeedback);
    }

    @GetMapping("/average/{programId}")
    public ResponseEntity<Double> getAverageRating(@PathVariable Long programId) {
        double average = feedbackService.getAverageRatingForProgram(programId);
        return ResponseEntity.ok(average);
    }
    // Méthode pour analyser un feedback
    @PutMapping("/diagnose/{feedbackId}")
    public ResponseEntity<Feedback> diagnoseFeedback(@PathVariable Long feedbackId) {
        Feedback diagnosedFeedback = feedbackService.diagnoseFeedback(feedbackId);
        return ResponseEntity.ok(diagnosedFeedback);
    }
    // Endpoint pour récupérer tous les feedbacks
    @GetMapping("/all")
    public ResponseEntity<List<Feedback>> getAllFeedbacks() {
        List<Feedback> feedbacks = feedbackService.getAllFeedbacks();  // Assurez-vous que la méthode existe dans le service
        return ResponseEntity.ok(feedbacks);
    }



}