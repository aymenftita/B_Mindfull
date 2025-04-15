package tn.esprit.mindfull.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    private String name;

    @Enumerated(EnumType.STRING)
    private Role role;  // doctor or patient

    // Prescriptions created by this doctor
    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL)
    private List<Prescription> createdPrescriptions;

    // Prescriptions belonging to this patient
    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL)
    private List<Prescription> prescriptions;

    // Notes written by this doctor
    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL)
    private List<PatientNote> writtenNotes;

    // Notes about this patient
    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL)
    private List<PatientNote> receivedNotes;

}
