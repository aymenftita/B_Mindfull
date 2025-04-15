package tn.esprit.mindfull.Respository;

import tn.esprit.mindfull.entity.Prescription;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PrescriptionRepository extends JpaRepository<Prescription, Long> {
}
