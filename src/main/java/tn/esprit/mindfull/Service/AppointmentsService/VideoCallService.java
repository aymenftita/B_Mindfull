package tn.esprit.mindfull.Service.AppointmentsService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.mindfull.dto.Appointmentsdto.VideoRoomDTO;
import tn.esprit.mindfull.entity.Appointment.Appointment;
import tn.esprit.mindfull.entity.Appointment.VideoCall;
import tn.esprit.mindfull.entity.Appointment.VideoStatus;
import tn.esprit.mindfull.exception.ResourceNotFoundException;
import tn.esprit.mindfull.Repository.AppointmentsRepository.AppointmentRepository;
import tn.esprit.mindfull.Repository.AppointmentsRepository.VideoCallRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@RequiredArgsConstructor
@Slf4j
public class VideoCallService {

    private final VideoCallRepository videoCallRepository;
    private final AppointmentRepository appointmentRepository;
    private final ConcurrentHashMap<String, AtomicInteger> participantCounts = new ConcurrentHashMap<>();

    @Transactional
    public VideoCall findOrCreateVideoCall(Integer appointmentId) {
        return videoCallRepository.findByAppointmentAppointmentId(appointmentId)
                .orElseGet(() -> createNewVideoCall(appointmentId));
    }

    private VideoCall createNewVideoCall(Integer appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found with id: " + appointmentId));
        VideoCall videoCall = new VideoCall(appointment);
        videoCall.setStatus(VideoStatus.PENDING);
        return videoCallRepository.save(videoCall);
    }

    @Transactional
    public VideoRoomDTO createVideoRoom(Integer appointmentId) {
        VideoCall videoCall = findOrCreateVideoCall(appointmentId);

        if (videoCall.getRoomId() == null) {
            String roomId = generateRoomId();
            videoCall.setRoomId(roomId);
            videoCall.setAccessLink("/video-call/" + roomId);
            participantCounts.put(roomId, new AtomicInteger(0));
            videoCall = videoCallRepository.save(videoCall);
        }

        return buildVideoRoomDTO(videoCall);
    }

    private String generateRoomId() {
        return UUID.randomUUID().toString().substring(0, 8);
    }

    private VideoRoomDTO buildVideoRoomDTO(VideoCall videoCall) {
        Appointment appointment = videoCall.getAppointment();
        return VideoRoomDTO.builder()
                .roomId(videoCall.getRoomId())
                .appointmentId(appointment.getAppointmentId())
                .status(videoCall.getStatus())
                .patientId(appointment.getPatient().getId().toString())
                .professionalId(appointment.getProfessional().getId().toString())
                .build();
    }

    public VideoCall findByRoomId(String roomId) {
        VideoCall videoCall = videoCallRepository.findByRoomId(roomId)
                .orElseThrow(() -> new ResourceNotFoundException("Video room not found"));

        participantCounts.compute(roomId, (k, v) -> {
            if (v == null) return new AtomicInteger(1);
            v.incrementAndGet();
            return v;
        });

        return videoCall;
    }

    public int getParticipantCount(String roomId) {
        return participantCounts.getOrDefault(roomId, new AtomicInteger(0)).get();
    }

    @Transactional
    public VideoCall updateVideoCallStatus(Integer appointmentId, VideoStatus status) {
        VideoCall videoCall = findOrCreateVideoCall(appointmentId);
        videoCall.setStatus(status);
        updateTimestamps(videoCall, status);
        return videoCallRepository.save(videoCall);
    }

    private void updateTimestamps(VideoCall videoCall, VideoStatus status) {
        if (status == VideoStatus.ACTIVE) {
            videoCall.setStartedAt(LocalDateTime.now());
        } else if (status == VideoStatus.ENDED) {
            videoCall.setEndedAt(LocalDateTime.now());
            participantCounts.remove(videoCall.getRoomId());
        }
    }

    @Transactional
    public VideoCall saveNotes(Integer appointmentId, String notes) {
        VideoCall videoCall = findOrCreateVideoCall(appointmentId);
        videoCall.setNotes(notes);
        return videoCallRepository.save(videoCall);
    }

    public List<VideoCall> findAllActiveVideoCalls() {
        return videoCallRepository.findByStatus(VideoStatus.ACTIVE);
    }

    @Transactional
    public VideoCall endVideoCall(Integer appointmentId) {
        return updateVideoCallStatus(appointmentId, VideoStatus.ENDED);
    }
}