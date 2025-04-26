package tn.esprit.mindfull.service;

import tn.esprit.mindfull.entity.Prescription;

import java.util.HashMap;
import java.util.List;

public interface PrescriptionService {
    List<Prescription> getAllPrescriptions();
    Prescription getPrescriptionById(int id);
    Prescription createPrescription(Prescription prescription);
    Prescription updatePrescription(int id, Prescription prescription);
    void deletePrescription(int id);
    HashMap<String, Float> getStatistics();
    HashMap<String, Float> getMedicationStatistics();
    List<Prescription> getPatientPrescription(int patientId);
}
