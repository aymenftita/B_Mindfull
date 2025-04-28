package tn.esprit.mindfull.Controller.AppointmentsController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.mindfull.dto.Appointmentsdto.VideoRoomDTO;
import tn.esprit.mindfull.entity.Appointment.VideoCall;
import tn.esprit.mindfull.entity.Appointment.VideoStatus;
import tn.esprit.mindfull.Service.AppointmentsService.VideoCallService;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("api/video-calls")
@RequiredArgsConstructor
@Slf4j
public class VideoCallController {

    private final VideoCallService videoCallService;

    @PostMapping("/appointment/{appointmentId}/room")
    public ResponseEntity<VideoRoomDTO> createVideoRoom(@PathVariable Integer appointmentId) {
        return ResponseEntity.ok(videoCallService.createVideoRoom(appointmentId));
    }

    @GetMapping("/appointment/{appointmentId}")
    public ResponseEntity<VideoCall> getVideoCallForAppointment(@PathVariable Integer appointmentId) {
        return ResponseEntity.ok(videoCallService.findOrCreateVideoCall(appointmentId));
    }

    @GetMapping("/room/{roomId}")
    public ResponseEntity<VideoCall> getVideoCallByRoomId(@PathVariable String roomId) {
        return ResponseEntity.ok(videoCallService.findByRoomId(roomId));
    }

    @GetMapping("/room/{roomId}/participants")
    public ResponseEntity<Integer> getParticipantCount(@PathVariable String roomId) {
        return ResponseEntity.ok(videoCallService.getParticipantCount(roomId));
    }

    @PutMapping("/appointment/{appointmentId}/status")
    public ResponseEntity<VideoCall> updateVideoStatus(
            @PathVariable Integer appointmentId,
            @RequestParam VideoStatus status) {
        return ResponseEntity.ok(videoCallService.updateVideoCallStatus(appointmentId, status));
    }

    @PostMapping("/appointment/{appointmentId}/notes")
    public ResponseEntity<VideoCall> saveNotes(
            @PathVariable Integer appointmentId,
            @RequestParam String notes) {
        return ResponseEntity.ok(videoCallService.saveNotes(appointmentId, notes));
    }

    @PostMapping("/appointment/{appointmentId}/end")
    public ResponseEntity<VideoCall> endVideoCall(@PathVariable Integer appointmentId) {
        return ResponseEntity.ok(videoCallService.endVideoCall(appointmentId));
    }
}