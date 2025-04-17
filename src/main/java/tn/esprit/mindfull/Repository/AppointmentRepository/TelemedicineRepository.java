package tn.esprit.mindfull.Repository.AppointmentRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.mindfull.entity.Appointment.Telemedicine;
import tn.esprit.mindfull.entity.Appointment.VideoStatus;

import java.util.List;
import java.util.Optional;

@Repository
public interface TelemedicineRepository extends JpaRepository<Telemedicine, Integer> {
    List<Telemedicine> findByConsultationLinkIsNotNull();
    Optional<Telemedicine> findByVideoRoomId(String videoRoomId);
    List<Telemedicine> findByVideoStatus(VideoStatus status);
}
