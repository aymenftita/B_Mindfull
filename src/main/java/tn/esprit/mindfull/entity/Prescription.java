package tn.esprit.mindfull.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Prescription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long prescriptionId;

    private String medicationName;
    private String dosage;

    // Doctor who created the prescription
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "doctor_id")  // References User (role=DOCTOR)
    private User doctor;

    // Patient who owns the prescription
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "patient_id")  // References User (role=PATIENT)
    private User patient;
    @JsonIgnore
    @OneToMany(mappedBy = "prescription", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PatientNote> patientNotes = new ArrayList<>();
}
