package tn.esprit.mindfull.Controller.CoachingController;

import org.json.JSONObject;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.net.*;
import java.util.*;

@RestController
@RequestMapping("/api/ai")
public class Ai_FeedBackController {
    @PostMapping("/diagnose-feedback")
    public ResponseEntity<Map<String, String>> diagnoseFeedback(@RequestBody Map<String, String> payload) {
        String feedback = payload.get("comment");

        try {
            URL url = new URL("http://localhost:11434/api/generate");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/json");

            // Create a JSON object to properly handle escaping
            JSONObject requestBody = new JSONObject();
            requestBody.put("model", "mistral");
            requestBody.put("prompt", "Please analyze the following user feedback and provide a diagnosis:\n" + feedback);
            requestBody.put("stream", false);

            // Sending the request JSON to the Mistral API
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = requestBody.toString().getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            int responseCode = connection.getResponseCode();
            System.out.println("Response Code: " + responseCode);

            StringBuilder response = new StringBuilder();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                try (BufferedReader br = new BufferedReader(
                        new InputStreamReader(connection.getInputStream(), "utf-8"))) {
                    String line;
                    while ((line = br.readLine()) != null) {
                        response.append(line.trim());
                    }
                }
                JSONObject json = new JSONObject(response.toString());
                String diagnosisResult = json.getString("response");

                Map<String, String> resultMap = new HashMap<>();
                resultMap.put("diagnosis", diagnosisResult);
                return ResponseEntity.ok(resultMap);
            } else {
                try (BufferedReader errorReader = new BufferedReader(
                        new InputStreamReader(connection.getErrorStream(), "utf-8"))) {
                    StringBuilder errorResponse = new StringBuilder();
                    String errorLine;
                    while ((errorLine = errorReader.readLine()) != null) {
                        errorResponse.append(errorLine);
                    }
                    System.out.println("Error Response: " + errorResponse.toString());
                }
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }

        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}