package tn.esprit.mindfull.Respository;


import tn.esprit.mindfull.entity.PatientNote;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientNoteRepository extends JpaRepository<PatientNote, Long> {
}