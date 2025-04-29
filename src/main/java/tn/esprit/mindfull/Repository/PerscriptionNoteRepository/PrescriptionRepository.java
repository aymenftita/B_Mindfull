package tn.esprit.mindfull.Repository.PerscriptionNoteRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tn.esprit.mindfull.entity.PerscriptionNote.Prescription;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PrescriptionRepository extends JpaRepository<Prescription, Integer> {

    // Optional: Custom queries to support business rules
    List<Prescription> findByDoctorId(int doctorId);

    List<Prescription> findByDoctorIdAndCreationDateBetween(
            int doctorId,
            LocalDate startDate,
            LocalDate endDate
    );

    List<Prescription> findByExpirationDateBefore(LocalDate currentDate);
    long countByCreationDate(LocalDate date);
    List<Prescription> findByPatientId(int patientId);
    @Query("SELECT p FROM Prescription p WHERE DATE(p.expirationDate) = DATE(:date)")
    List<Prescription> findByExpirationDate(@Param("date") LocalDate date);



}