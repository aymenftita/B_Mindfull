package tn.esprit.mindfull.Service.PerscriptionNoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import tn.esprit.mindfull.entity.PerscriptionNote.NotificationMessage;
import tn.esprit.mindfull.entity.PerscriptionNote.Prescription;
import tn.esprit.mindfull.Repository.PerscriptionNoteRepository.PrescriptionRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class NotificationService {

    private final SimpMessagingTemplate messagingTemplate;
    private final PrescriptionRepository prescriptionRepository;
    private static final AtomicInteger notificationCounter = new AtomicInteger(0);

    // Store active sessions - this would be populated by a SessionListener
    private static final Set<String> activeSessions = ConcurrentHashMap.newKeySet();

    @Autowired
    public NotificationService(SimpMessagingTemplate messagingTemplate, PrescriptionRepository prescriptionRepository) {
        this.messagingTemplate = messagingTemplate;
        this.prescriptionRepository = prescriptionRepository;
        // Check immediately upon service startup
        checkExpiringPrescriptions();
    }

    // Method to register a session (called from WebSocket event handler)
    public void registerSession(String sessionId) {
        activeSessions.add(sessionId);
        System.out.println("Session registered: " + sessionId);
    }

    // Method to unregister a session (called from WebSocket event handler)
    public void unregisterSession(String sessionId) {
        activeSessions.remove(sessionId);
        System.out.println("Session unregistered: " + sessionId);
    }

    // Run daily at midnight to check for prescriptions expiring tomorrow
    @Scheduled(cron = "0 0 0 * * ?") // Runs daily at midnight
    public void checkExpiringPrescriptions() {
        LocalDate tomorrow = LocalDate.now().plusDays(1); // Get tomorrow's date
        List<Prescription> expiringPrescriptions = prescriptionRepository.findByExpirationDate(tomorrow);

        if (expiringPrescriptions.isEmpty()) {
            System.out.println("No prescriptions expiring tomorrow."); // Debugging
        }

        for (Prescription prescription : expiringPrescriptions) {
            broadcastExpirationNotification(prescription);
        }
    }

    // Run every hour to catch any newly added prescriptions that expire tomorrow
    @Scheduled(cron = "0 0 * * * ?") // Runs every hour
    public void hourlyCheckExpiringPrescriptions() {
        checkExpiringPrescriptions();
    }

    // Also allow manual checking
    public void manualCheckExpiringPrescriptions() {
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        List<Prescription> expiringPrescriptions = prescriptionRepository.findByExpirationDate(tomorrow);

        for (Prescription prescription : expiringPrescriptions) {
            broadcastExpirationNotification(prescription);
        }
    }

    // Send notification to all active sessions
    private void broadcastExpirationNotification(Prescription prescription) {
        NotificationMessage notification = new NotificationMessage(
                notificationCounter.incrementAndGet(),
                "Prescription #" + prescription.getId() + " will expire tomorrow.",
                LocalDateTime.now(),
                "EXPIRATION_WARNING",
                0, // No specific user ID since we're using session IDs
                false
        );

        // Broadcast to all active sessions
        for (String sessionId : activeSessions) {
            messagingTemplate.convertAndSend("/topic/notifications/" + sessionId, notification);
            System.out.println("Notification sent to session: " + sessionId);
        }
    }

    public void checkExpiringPrescriptionsForSession(String sessionId) {
        if (sessionId == null) return;

        LocalDate tomorrow = LocalDate.now().plusDays(1);
        List<Prescription> expiringPrescriptions = prescriptionRepository.findByExpirationDate(tomorrow);

        System.out.println("Found " + expiringPrescriptions.size() + " prescriptions expiring tomorrow for session: " + sessionId);

        for (Prescription prescription : expiringPrescriptions) {
            NotificationMessage notification = new NotificationMessage(
                    notificationCounter.incrementAndGet(),
                    "Prescription #" + prescription.getId() + " will expire tomorrow.",
                    LocalDateTime.now(),
                    "EXPIRATION_WARNING",
                    0,
                    false
            );

            messagingTemplate.convertAndSend("/topic/notifications/" + sessionId, notification);
            System.out.println("Sent notification about prescription " + prescription.getId() + " to session: " + sessionId);
        }
    }
}