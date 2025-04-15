package tn.esprit.mindfull.dtos;

import lombok.Data;

@Data
public class PrescriptionDto {
    private Long prescriptionId;
    private String medicationName;
    private String dosage;
    private Long doctorId;       // ID only
    private String doctorName;   // Optional: if you want to show doctor name
    private Long patientId;      // ID only
    private String patientName;  // Optional: if you want to show patient name
}