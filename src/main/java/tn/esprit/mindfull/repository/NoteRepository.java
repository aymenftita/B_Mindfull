package tn.esprit.mindfull.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.mindfull.entity.Note;

import java.util.List;

@Repository
public interface NoteRepository extends JpaRepository<Note, Integer> {

    // Optional: Custom queries if needed, for example:
    List<Note> findByDoctorId(int doctorId);
    List<Note> findByCoachId(int coachId);
    List<Note> findByPatientId(int patientId);
}