package tn.esprit.mindfull.Service.PerscriptionNoteService;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.mindfull.Repository.UserRepository.UserRepository;
import tn.esprit.mindfull.dto.PrescriptionNotedto.PrescriptionRequestDTO;
import tn.esprit.mindfull.entity.PerscriptionNote.Medication;
import tn.esprit.mindfull.entity.PerscriptionNote.Prescription;
import tn.esprit.mindfull.Repository.PerscriptionNoteRepository.MedicationRepository;
import tn.esprit.mindfull.Repository.PerscriptionNoteRepository.PrescriptionRepository;
import tn.esprit.mindfull.entity.User.User;

import java.time.LocalDate;
import java.time.Period;
import java.util.HashMap;
import java.util.List;

@Service
@AllArgsConstructor
public class PrescriptionServiceImpl implements PrescriptionService {

    private final PrescriptionRepository prescriptionRepository;
    private final MedicationRepository medicationRepository;
    @Autowired
    private UserRepository userRepository;
   /* @Override
    public Prescription createPrescription(Prescription prescription) {
        // ensure medication list is not null
        if (prescription.getListMedicaton() == null || prescription.getListMedicaton().isEmpty()) {
            throw new IllegalArgumentException("Prescription must contain at least one medication.");
        }

        prescription.setExpirationDate(getPrescriptionExpirationDate(prescription.getListMedicaton()));
        prescription.setCreationDate(LocalDate.now());

        // Set bidirectional relationship properly
        for (Medication medication : prescription.getListMedicaton()) {
            medication.setPrescription(prescription);
        }

        // Save the prescription with its medications
        return prescriptionRepository.save(prescription);
    }
*/
   public Prescription createPrescription(PrescriptionRequestDTO dto) {
       Prescription prescription = new Prescription();

       // Get patient (required)
       User patient = userRepository.findById(Long.valueOf(dto.getPatientId()))
               .orElseThrow(() -> new EntityNotFoundException("Patient not found with ID: " + dto.getPatientId()));
       prescription.setPatient(patient);

       // Get doctor (required)
       User doctor = userRepository.findById(Long.valueOf(dto.getDoctorId()))
               .orElseThrow(() -> new EntityNotFoundException("Doctor not found with ID: " + dto.getDoctorId()));
       prescription.setDoctor(doctor);

       // Get coach (optional)
       if (dto.getCoachId() != null) {
           User coach = userRepository.findById(Long.valueOf(dto.getCoachId()))
                   .orElseThrow(() -> new EntityNotFoundException("Coach not found with ID: " + dto.getCoachId()));
           prescription.setCoach(coach);
       }

       // Map other fields
       prescription.setAuthorName(dto.getAuthorName());
       prescription.setDiagnosis(dto.getDiagnosis());
       prescription.setNotes(dto.getNotes());
       prescription.setExpirationDate(dto.getExpirationDate());
       prescription.setCreationDate(LocalDate.now());

       // Add medications
       dto.getMedications().forEach(medDto -> {
           Medication medication = new Medication();
           medication.setMedicationName(medDto.getMedicationName());
           medication.setDirections(medDto.getDirections());
           medication.setDuration(medDto.getDuration());
           prescription.addMedication(medication);
       });

       return prescriptionRepository.save(prescription);
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

            // Properly handle the medication list update
            if (updated.getListMedicaton() != null) {
                // Clear existing medications and add the new ones
                // This properly maintains the bidirectional relationship
                existing.getListMedicaton().clear();

                // Set the prescription reference for each medication and add to list
                for (Medication medication : updated.getListMedicaton()) {
                    medication.setPrescription(existing);
                    existing.getListMedicaton().add(medication);
                }

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
            if (prescription.getPatient() != null && prescription.getPatient().getBirth() != null) {
                String birthDate = prescription.getPatient().getBirth().toString();
                int age = Period.between(LocalDate.parse(birthDate), LocalDate.now()).getYears();

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
