package tn.esprit.mindfull.controller;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class AiController {

    private final RestTemplate restTemplate = new RestTemplate();
    @PostMapping("/ai")
    public ResponseEntity<String> generateResponse(@RequestBody OllamaRequest request) {
        String url = "http://localhost:11434/api/generate";



        HttpEntity<OllamaRequest> entity = new HttpEntity<>(request);

        return restTemplate.postForEntity(url, entity, String.class);
    }

    // Same DTO as above
    public static class OllamaRequest {
        private String model;
        private String prompt;

    }
}

