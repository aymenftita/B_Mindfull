package tn.esprit.mindfull.Repository.AppointmentRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tn.esprit.mindfull.entity.Appointment.Appointment;
import tn.esprit.mindfull.entity.Appointment.VideoCall;
import tn.esprit.mindfull.entity.Appointment.VideoStatus;

import java.util.List;
import java.util.Optional;

@Repository
public interface VideoCallRepository extends JpaRepository<VideoCall, Integer> {
    Optional<VideoCall> findByRoomId(String roomId);
    List<VideoCall> findByStatus(VideoStatus status);
    @Query("SELECT v FROM VideoCall v WHERE v.appointment.appointmentId = :appointmentId")
    Optional<VideoCall> findByAppointmentAppointmentId(Integer appointmentId);
}