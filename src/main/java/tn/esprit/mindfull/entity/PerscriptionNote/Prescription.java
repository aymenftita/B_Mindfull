package tn.esprit.mindfull.entity.PerscriptionNote;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@Entity
public class Prescription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "doctor_id")
    private User doctor;

    @ManyToOne
    @JoinColumn(name = "coach_id")
    private User coach;

    @ManyToOne
    @JoinColumn(name = "patient_id")
    private User patient;

    private String authorName;
    private String diagnosis;

    @OneToMany(mappedBy = "prescription", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Medication> listMedicaton = new ArrayList<>();

    // Helper method to manage the bidirectional relationship
    public void addMedication(Medication medication) {
        listMedicaton.add(medication);
        medication.setPrescription(this);
    }

    // Helper method to safely remove a medication
    public void removeMedication(Medication medication) {
        listMedicaton.remove(medication);
        medication.setPrescription(null);
    }

    // Clear and set all medications at once
    public void setMedications(List<Medication> medications) {
        this.listMedicaton.clear();
        if (medications != null) {
            medications.forEach(this::addMedication);
        }
    }

    @Column(length = 1000)
    private String notes;

    private LocalDate creationDate;
    private LocalDate updateDate;
    private LocalDate expirationDate;
}