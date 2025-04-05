package tn.esprit.mindfull.Repository.AppointmentRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.mindfull.entity.Appointment.Telemedicine;

import java.util.List;

@Repository
public interface TelemedicineRepository extends JpaRepository<Telemedicine, Integer> {
    List<Telemedicine> findByConsultationLinkIsNotNull();
}
