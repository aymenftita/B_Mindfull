package tn.esprit.mindfull.Repository.PerscriptionNoteRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.mindfull.entity.PerscriptionNote.Medication;

@Repository
public interface MedicationRepository extends JpaRepository<Medication, Integer> {
}
