package tn.esprit.mindfull.dto.PrescriptionNotedto;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class PrescriptionRequestDTO {
    private Integer patientId;

    private Integer doctorId;

    private Integer coachId; // Optional

    private String authorName;

    private String diagnosis;

    private String notes;

    private LocalDate expirationDate;

    private List<MedicationDTO> medications;
}