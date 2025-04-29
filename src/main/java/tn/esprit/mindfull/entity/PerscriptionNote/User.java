package tn.esprit.mindfull.entity.PerscriptionNote;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String fullName;
    private String email;

    @Enumerated(EnumType.STRING)
    private Role role;

    // Doctor or Coach Notes & Prescriptions
    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Note> doctorNotes= new ArrayList<>();;;

    @OneToMany(mappedBy = "coach", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Note> coachNotes= new ArrayList<>();;;

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Note> patientNotes= new ArrayList<>();;;

    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Prescription> doctorPrescriptions = new ArrayList<>();;

    private LocalDate dateDeNaissance;

    @OneToMany(mappedBy = "coach", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Prescription> coachPrescriptions = new ArrayList<>();;

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Prescription> patientPrescriptions = new ArrayList<>();;



}
