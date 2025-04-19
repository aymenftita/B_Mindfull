package tn.esprit.mindfull.service.ForumService;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.HashMap;
import java.util.Map;

@Service
public class NotificationService {

    private final CopyOnWriteArrayList<SseEmitter> emitters = new CopyOnWriteArrayList<>();

    public SseEmitter subscribe() {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        emitters.add(emitter);

        emitter.onCompletion(() -> emitters.remove(emitter));
        emitter.onTimeout(() -> emitters.remove(emitter));

        return emitter;
    }

    public void sendNotification(String message) {
        // Create a structured notification (example: message from a comment)
        try {
            // Assuming the message is passed in a simple format, we create a JSON object with more info
            Map<String, String> notification = new HashMap<>();
            notification.put("type", "comment");
            notification.put("user", "User1");  // Replace with dynamic username
            notification.put("message", message);

            // Convert map to JSON string
            ObjectMapper mapper = new ObjectMapper();
            String jsonNotification = mapper.writeValueAsString(notification);

            // Send notification to all connected clients
            for (SseEmitter emitter : emitters) {
                try {
                    emitter.send(SseEmitter.event().name("notification").data(jsonNotification));
                } catch (IOException e) {
                    emitters.remove(emitter);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
