package tn.esprit.mindfull.Controller.PerscriptionNoteController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tn.esprit.mindfull.Service.PerscriptionNoteService.NotificationService;
@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    @Autowired
    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @MessageMapping("/check-expiring-prescriptions")
    public void checkExpiringPrescriptions(StompHeaderAccessor headerAccessor) {
        String sessionId = headerAccessor.getSessionId();
        System.out.println("Manual check requested by session: " + sessionId);
        notificationService.checkExpiringPrescriptionsForSession(sessionId);
    }
}