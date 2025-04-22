package tn.esprit.mindfull.Service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.*;

@Service
public class AiDoctorNoteService {

    @Value("${openrouter.api.key}")
    private String apiKey;

    private static final String API_URL = "https://openrouter.ai/api/v1/chat/completions";

    public String generateDoctorNote(String summary) {
        RestTemplate restTemplate = new RestTemplate();

        Map<String, Object> message = Map.of(
                "role", "user",
                "content", "Generate a professional and concise medical consultation note based on the following summary:\n" + summary
        );

        Map<String, Object> body = Map.of(
                "model", "mistralai/mistral-small",
                "messages", List.of(message)
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);
        headers.set("HTTP-Referer", "http://localhost"); // Required by OpenRouter
        headers.set("X-Title", "doctor-notes");

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(API_URL, request, Map.class);
            List<Map<String, Object>> choices = (List<Map<String, Object>>) response.getBody().get("choices");
            Map<String, Object> messageMap = (Map<String, Object>) choices.get(0).get("message");
            return (String) messageMap.get("content");
        } catch (Exception e) {
            e.printStackTrace();
            return "AI error: " + e.getMessage();
        }
    }
}
