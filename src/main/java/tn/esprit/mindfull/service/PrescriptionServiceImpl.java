package tn.esprit.mindfull.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.mindfull.entity.Medication;
import tn.esprit.mindfull.entity.Prescription;
import tn.esprit.mindfull.repository.MedicationRepository;
import tn.esprit.mindfull.repository.PrescriptionRepository;

import java.time.LocalDate;
import java.time.Period;
import java.util.HashMap;
import java.util.List;

@Service
@AllArgsConstructor
public class PrescriptionServiceImpl implements PrescriptionService {

    private final PrescriptionRepository prescriptionRepository;
    private final MedicationRepository medicationRepository;
    @Override
    public Prescription createPrescription(Prescription prescription) {
        List<Medication> medications = prescription.getListMedicaton();

        // ensure medication list is not null
        if (medications == null || medications.isEmpty()) {
            throw new IllegalArgumentException("Prescription must contain at least one medication.");
        }

        prescription.setExpirationDate(getPrescriptionExpirationDate(medications));
        prescription.setCreationDate(LocalDate.now());

        // save prescription first to get the ID
        Prescription savedPrescription = prescriptionRepository.save(prescription);

        // assign prescription and save medications
        for (Medication m : medications) {
            m.setPrescription(savedPrescription);
        }
        medicationRepository.saveAll(medications); // saveAll is more efficient than individual saves

        return savedPrescription;
    }

    @Override
    public List<Prescription> getAllPrescriptions() {
        return prescriptionRepository.findAll();
    }

    @Override
    public Prescription getPrescriptionById(int id) {
        return prescriptionRepository.findById(id).orElse(null);
    }

    @Override
    public void deletePrescription(int id) {
        prescriptionRepository.deleteById(id);
    }

    @Override
    public Prescription updatePrescription(int id, Prescription updated) {
        return prescriptionRepository.findById(id).map(existing -> {
            existing.setUpdateDate(LocalDate.now());
            existing.setDiagnosis(updated.getDiagnosis());
            existing.setNotes(updated.getNotes());

            // update medication list if needed
            if (updated.getListMedicaton() != null) {
                existing.setListMedicaton(updated.getListMedicaton());
                existing.setExpirationDate(getPrescriptionExpirationDate(updated.getListMedicaton()));
            } else {
                existing.setExpirationDate(getPrescriptionExpirationDate(existing.getListMedicaton()));
            }

            return prescriptionRepository.save(existing);
        }).orElse(null);
    }


    public LocalDate getPrescriptionExpirationDate(List<Medication> medicationList) {
        if (medicationList == null || medicationList.isEmpty()) {
            return LocalDate.now(); // No medications, return today
        }

        int shortestDuration = medicationList.stream()
                .filter(m -> m != null && m.getDuration() > 0)
                .mapToInt(Medication::getDuration)
                .min()
                .orElse(0); // Default to 0 if no valid durations

        return LocalDate.now().plusDays(shortestDuration);
    }

    @Override
    public HashMap<String, Float> getStatistics() {
        HashMap<String, Float> statistics = new HashMap<>();


        float totalPrescriptions = (float) this.prescriptionRepository.findAll().size();
        statistics.put("totalPrescriptions", totalPrescriptions);


        LocalDate today = LocalDate.now();
        LocalDate oneWeekAgo = today.minusDays(7);


        long todayCount = this.prescriptionRepository.countByCreationDate(today);
        long weekAgoCount = this.prescriptionRepository.countByCreationDate(oneWeekAgo);


        float growthRate = 0f;
        if (weekAgoCount > 0) {
            growthRate = ((float)(todayCount - weekAgoCount) / weekAgoCount) * 100;
        } else if (todayCount > 0) {
            growthRate = 100f; // From 0 to something = 100% growth
        }

        statistics.put("growthRate", growthRate); // Can be positive or negative
        return statistics;

}
public HashMap<String, Float> getMedicationStatistics() {
        HashMap<String, Float> stats = new HashMap<>();

        // counters
        float infants = 0;
        float adults = 0;
        float elderly = 0;

        List<Prescription> prescriptions = prescriptionRepository.findAll();

        for (Prescription prescription : prescriptions) {
            if (prescription.getPatient() != null && prescription.getPatient().getDateDeNaissance() != null) {
                LocalDate birthDate = prescription.getPatient().getDateDeNaissance();
                int age = Period.between(birthDate, LocalDate.now()).getYears();

                if (age < 12) {
                    infants++;
                } else if (age < 60) {
                    adults++;
                } else {
                    elderly++;
                }
            }
        }
        stats.put("Infants", infants);
        stats.put("Adults", adults);
        stats.put("Elderly", elderly);

        return stats;
    }

    @Override
    public List<Prescription> getPatientPrescription(int patientId) {
        return prescriptionRepository.findByPatientId(patientId);
    }
}
