package tn.esprit.mindfull.Service.CoachingService;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import tn.esprit.mindfull.Repository.CoachingRepository.FeedbackRepository;
import tn.esprit.mindfull.Repository.CoachingRepository.ProgramContentRepository;
import tn.esprit.mindfull.Repository.UserRepository.UserRepository;
import tn.esprit.mindfull.entity.Coaching.Feedback;
import tn.esprit.mindfull.entity.Coaching.ProgramContent;
import tn.esprit.mindfull.entity.User.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@AllArgsConstructor
public class FeedbackService {
    private final FeedbackRepository feedbackRepository;
    private final ProgramContentRepository programContentRepository;
   // @Autowired
  //  private NotificationService notificationService;
    private final UserRepository userRepository;
    @Autowired
    private AIService aiService;
    public Feedback addFeedback(Feedback feedback, Long patientId, Long contentId) {
        if (feedback.getPatient() == null) {
            User defaultPatient = userRepository.findById(patientId)
                    .orElseThrow(() -> new RuntimeException("Patient par défaut non trouvé."));
            feedback.setPatient(defaultPatient);
        }
        if (contentId <= 0) {
            throw new IllegalArgumentException("ID de contenu invalide : " + contentId);
        }
        // Vérifier si le contenu du programme existe
        ProgramContent content = programContentRepository.findById(contentId)
                .orElseThrow(() -> new RuntimeException("Contenu du programme non trouvé avec l'ID : " + contentId));

        feedback.setProgramContent(content);


        // Sauvegarder le feedback
        Feedback savedFeedback = feedbackRepository.save(feedback);

        // Envoyer une notification au coach (commenté ici, à décommenter si nécessaire)
        // notificationService.notifyCoachAboutFeedback(content.getProgram().getUser(), content);

        return savedFeedback;
    }
    // Ajouter la méthode pour récupérer un feedback par ID
    public String getFeedbackById(Long id) {
        Optional<Feedback> feedbackOpt = feedbackRepository.findById(id);
        if (feedbackOpt.isPresent()) {
            return feedbackOpt.get().getComment();  // Supposons que la classe Feedback a un champ "comment"
        }
        return "Feedback non trouvé.";
    }
    // Cette méthode envoie le texte du feedback à Ollama et récupère le diagnostic
    public String analyzeFeedbackWithAI(Feedback feedback) {
        String feedbackText = feedback.getComment();
        return callOllamaAPI(feedbackText);
    }

    // Méthode pour envoyer une requête à l'API Ollama
    private String callOllamaAPI(String feedbackText) {
        String ollamaEndpoint = "https://api.ollama.com/diagnostic"; // URL de l'API Ollama

        // Créez un RestTemplate pour envoyer des requêtes HTTP
        RestTemplate restTemplate = new RestTemplate();

        // Créez les en-têtes HTTP (Headers) pour spécifier que le contenu est en JSON
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Créez le corps de la requête (payload) qui contient le texte du feedback
        Map<String, String> body = new HashMap<>();
        body.put("feedback", feedbackText);

        // Créez l'entité HTTP avec les en-têtes et le corps
        HttpEntity<Map<String, String>> entity = new HttpEntity<>(body, headers);

        // Effectuez la requête POST avec l'entité et récupérez la réponse
        ResponseEntity<String> response = restTemplate.exchange(ollamaEndpoint, HttpMethod.POST, entity, String.class);

        // Retourner la réponse du diagnostic généré par Ollama
        return response.getBody();
    }

    public double getAverageRatingForProgram(Long programId) {
        // Récupérer les retours de programme
        List<Feedback> feedbacks = feedbackRepository.findByProgramContent_Program_ProgramId(programId);

        // Calculer la moyenne des évaluations
        return feedbacks.stream()
                .mapToInt(Feedback::getRating)
                .average()
                .orElse(0.0);
    }
    // Récupérer tous les feedbacks
    public List<Feedback> getAllFeedbacks() {
        return feedbackRepository.findAll();  // Assurez-vous que votre repository est bien configuré pour cela
    }
    public Feedback diagnoseFeedback(Long feedbackId) {
        Feedback feedback = feedbackRepository.findById(feedbackId)
                .orElseThrow(() -> new RuntimeException("Feedback introuvable"));

        String diagnostic = aiService.getDiagnosis(feedback.getComment());
        feedback.setDiagnostic(diagnostic);

        return feedbackRepository.save(feedback);
    }

}