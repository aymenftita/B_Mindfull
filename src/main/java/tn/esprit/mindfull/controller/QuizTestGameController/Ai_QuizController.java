package tn.esprit.mindfull.controller.QuizTestGameController;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

@RestController
@CrossOrigin(origins = "*")
public class Ai_QuizController {

    private final RestTemplate restTemplate;

    public Ai_QuizController() {
        this.restTemplate = new RestTemplate();
        // Add JSON message converter
        this.restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
    }

    @PostMapping("/ai")
    public ResponseEntity<String> generateResponse(@RequestBody OllamaRequest request) {
        String url = "http://localhost:11434/api/generate";

        // Set headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<OllamaRequest> entity = new HttpEntity<>(request, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);
            return response;
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error calling Ollama API: " + e.getMessage());
        }
    }

    /*@PostMapping("/ai")
    public ResponseEntity<String> generateResponse(@RequestBody OllamaRequest request) {
        String url = "http://localhost:3000/api/generate";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<OllamaRequest> entity = new HttpEntity<>(request, headers);

        try {
            // Get raw streaming response
            ResponseEntity<String> ollamaResponse = restTemplate.postForEntity(url, entity, String.class);
            String rawResponse = ollamaResponse.getBody();

            // Process each JSON chunk
            StringBuilder combinedResponse = new StringBuilder();
            String[] jsonChunks = rawResponse.split("\n");  // Split by newline

            for (String chunk : jsonChunks) {
                if (!chunk.trim().isEmpty()) {
                    try {
                        JsonNode jsonNode = new ObjectMapper().readTree(chunk);
                        if (jsonNode.has("response")) {
                            combinedResponse.append(jsonNode.get("response").asText());
                        }
                    } catch (Exception e) {
                        // Skip invalid JSON chunks
                    }
                }
            }

            return ResponseEntity.ok(combinedResponse.toString());
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error calling Ollama API: " + e.getMessage());
        }
    }*/

    public static class OllamaRequest {
        private String model;
        private String prompt;

        // Getters and setters are REQUIRED for JSON serialization
        public String getModel() {
            return model;
        }

        public void setModel(String model) {
            this.model = model;
        }

        public String getPrompt() {
            return prompt;
        }

        public void setPrompt(String prompt) {
            this.prompt = prompt;
        }
    }
}