package tn.esprit.mindfull.Controller.UserController;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/shared_All")
public class GrammarController {

    @PostMapping("/grammar/correct")
    public ResponseEntity<String> correctGrammar(@RequestBody Map<String, String> payload) {
        String sentence = payload.get("sentence");
        String ollamaUrl = "http://localhost:11434/api/generate";

        RestTemplate restTemplate = new RestTemplate();

        Map<String, String> body = new HashMap<>();
        body.put("model", "mistral");
        body.put("prompt", "Correct the grammar of this sentence: '" + sentence + "'");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, String>> request = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(ollamaUrl, request, String.class);
            return ResponseEntity.ok(response.getBody());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error calling Ollama: " + e.getMessage());
        }
    }
}
