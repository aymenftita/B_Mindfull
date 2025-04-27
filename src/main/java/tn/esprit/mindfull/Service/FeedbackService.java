package tn.esprit.mindfull.Service;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import tn.esprit.mindfull.entity.*;
import tn.esprit.mindfull.Respository.FeedbackRepository;
import tn.esprit.mindfull.Respository.NotificationRepository;
import tn.esprit.mindfull.Respository.ProgramContentRepository;
import tn.esprit.mindfull.Respository.UserRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@AllArgsConstructor
public class FeedbackService {
    private final FeedbackRepository feedbackRepository;
    private final ProgramContentRepository programContentRepository;
    @Autowired
    private NotificationService notificationService;
    private final UserRepository userRepository;
    @Autowired
    private AIService aiService;
    public Feedback addFeedback(Feedback feedback, Long patientId, Long contentId) {
        // Vérifier si le patient existe
        User patient = userRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient non trouvé avec l'ID : " + patientId));

        // Vérifier si le contenu du programme existe
        ProgramContent content = programContentRepository.findById(contentId)
                .orElseThrow(() -> new RuntimeException("Contenu du programme non trouvé avec l'ID : " + contentId));

        // Associer le patient et le programme au feedback
        feedback.setPatient(patient);
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
}
