package tn.esprit.mindfull.Controller.ForumController;

import org.json.JSONObject;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.net.*;
import java.util.*;

@RestController
@RequestMapping("/api/ai")
@CrossOrigin(origins = "*")
public class Ai_ForumController {

    @PostMapping("/generate-post")
    public ResponseEntity<Map<String, String>> generatePost(@RequestBody Map<String, String> payload) {
        String prompt = payload.get("prompt");

        try {
            URL url = new URL("http://localhost:11434/api/generate");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/json");

            String requestJson = String.format("""
            {
                "model": "mistral",
                "prompt": "%s",
                "stream": false
            }
            """, prompt.replace("\"", "\\\""));

            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = requestJson.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            StringBuilder response = new StringBuilder();
            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(connection.getInputStream(), "utf-8"))) {
                String line;
                while ((line = br.readLine()) != null) {
                    response.append(line.trim());
                }
            }

            JSONObject json = new JSONObject(response.toString());
            String result = json.getString("response");

            Map<String, String> resultMap = new HashMap<>();
            resultMap.put("result", result);
            return ResponseEntity.ok(resultMap);

        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
