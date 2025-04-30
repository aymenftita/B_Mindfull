package tn.esprit.mindfull.Service.PerscriptionNoteService;

import tn.esprit.mindfull.dto.PrescriptionNotedto.PrescriptionRequestDTO;
import tn.esprit.mindfull.entity.PerscriptionNote.Prescription;

import java.util.HashMap;
import java.util.List;

public interface PrescriptionService {
    List<Prescription> getAllPrescriptions();
    Prescription getPrescriptionById(int id);
    Prescription createPrescription(PrescriptionRequestDTO dto);
    Prescription updatePrescription(int id, Prescription prescription);
    void deletePrescription(int id);
    HashMap<String, Float> getStatistics();
    HashMap<String, Float> getMedicationStatistics();
    List<Prescription> getPatientPrescription(int patientId);
}
