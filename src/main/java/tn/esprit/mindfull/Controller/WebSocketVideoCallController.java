package tn.esprit.mindfull.Controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import tn.esprit.mindfull.dto.WebRTCMessage;
import tn.esprit.mindfull.entity.Appointment.VideoStatus;
import tn.esprit.mindfull.Service.VideoCallService;

@Controller
@RequiredArgsConstructor
@Slf4j
public class WebSocketVideoCallController {

    private final SimpMessagingTemplate messagingTemplate;
    private final VideoCallService videoCallService;

    /**
     * Handle WebRTC signaling messages
     */
    @MessageMapping("/signal")
    public void handleSignalMessage(@Payload WebRTCMessage message) {
        log.info("Received signal message of type: {} from: {} to: {}",
                message.getType(), message.getFrom(), message.getTo());

        // Update appointment status based on message type
        Integer appointmentId = message.getAppointmentId();
        if (appointmentId != null) {
            if ("offer".equals(message.getType())) {
                // Call is starting
                videoCallService.updateVideoCallStatus(appointmentId, VideoStatus.ACTIVE);
            } else if ("leave".equals(message.getType())) {
                // Call is ending
                videoCallService.updateVideoCallStatus(appointmentId, VideoStatus.ENDED);
            }
        }

        // Forward the message to the recipient
        messagingTemplate.convertAndSend("/topic/signal/" + message.getTo(), message);
    }

    /**
     * Handle room join notification
     */
    @MessageMapping("/join-room")
    public void handleJoinRoom(@Payload WebRTCMessage message) {
        log.info("User {} joined room for appointment: {}", message.getFrom(), message.getAppointmentId());

        // Notify others in the room that someone has joined
        messagingTemplate.convertAndSend("/topic/room/" + message.getAppointmentId(), message);
    }
}