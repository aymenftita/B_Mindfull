package tn.esprit.mindfull.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.mindfull.entity.Medication;

@Repository
public interface MedicationRepository extends JpaRepository<Medication, Integer> {
}
