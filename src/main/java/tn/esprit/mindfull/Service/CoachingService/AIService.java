package tn.esprit.mindfull.Service.CoachingService;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class AIService {

    private static final Logger logger = LoggerFactory.getLogger(AIService.class);

    @Value("${openrouter.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public AIService(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    public String getDiagnosis(String feedbackComment) {
        String url = "https://api.ai-service.com/v2/diagnosis"; // Update to the correct endpoint

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + apiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        try {
            Map<String, String> requestBody = new HashMap<>();
            requestBody.put("comment", feedbackComment);
            String requestJson = objectMapper.writeValueAsString(requestBody);

            HttpEntity<String> entity = new HttpEntity<>(requestJson, headers);

            logger.info("Sending diagnosis request for comment: {}", feedbackComment);
            ResponseEntity<DiagnosisResponse> response = restTemplate.exchange(
                    url, HttpMethod.POST, entity, DiagnosisResponse.class
            );

            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody().getDiagnosis();
            } else {
                logger.warn("Non-successful response: {}", response.getStatusCode());
                return "Diagnostic non disponible: HTTP " + response.getStatusCode();
            }
        } catch (HttpClientErrorException e) {
            logger.error("Client error: {} - {}", e.getStatusCode(), e.getResponseBodyAsString());
            return "Erreur client: " + e.getStatusCode();
        } catch (RestClientException e) {
            logger.error("API connection error: {}", e.getMessage(), e);
            return "Erreur de connexion à l'API: " + e.getMessage();
        } catch (Exception e) {
            logger.error("Unexpected error: {}", e.getMessage(), e);
            return "Diagnostic non disponible en ce moment. Veuillez réessayer plus tard.";
        }
    }

    // DTO for API response
    public static class DiagnosisResponse {
        private String diagnosis;
        private String status;

        public String getDiagnosis() {
            return diagnosis;
        }

        public void setDiagnosis(String diagnosis) {
            this.diagnosis = diagnosis;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
}