package tn.esprit.mindfull.Mapper;

import org.springframework.stereotype.Component;
import tn.esprit.mindfull.dtos.PrescriptionDto;
import tn.esprit.mindfull.entity.Prescription;
import tn.esprit.mindfull.entity.User;

@Component
public class PrescriptionMapper {

    public PrescriptionDto toDto(Prescription prescription) {
        PrescriptionDto dto = new PrescriptionDto();
        dto.setPrescriptionId(prescription.getPrescriptionId());
        dto.setMedicationName(prescription.getMedicationName());
        dto.setDosage(prescription.getDosage());

        // Handle doctor mapping
        if (prescription.getDoctor() != null) {
            dto.setDoctorId(prescription.getDoctor().getUserId());
            dto.setDoctorName(prescription.getDoctor().getName() + " " + prescription.getDoctor().getName());
        }

        // Handle patient mapping
        if (prescription.getPatient() != null) {
            dto.setPatientId(prescription.getPatient().getUserId());
            dto.setPatientName(prescription.getPatient().getName() + " " + prescription.getPatient().getName());
        }

        return dto;
    }

    public Prescription toEntity(PrescriptionDto dto) {
        Prescription prescription = new Prescription();
        prescription.setPrescriptionId(dto.getPrescriptionId());
        prescription.setMedicationName(dto.getMedicationName());
        prescription.setDosage(dto.getDosage());

        // For doctor and patient, we only set the IDs which will be handled in service
        // The actual User objects will be set in service using repositories
        if (dto.getDoctorId() != null) {
            User doctor = new User();
            doctor.setUserId(dto.getDoctorId());
            prescription.setDoctor(doctor);
        }

        if (dto.getPatientId() != null) {
            User patient = new User();
            patient.setUserId(dto.getPatientId());
            prescription.setPatient(patient);
        }

        return prescription;
    }
}