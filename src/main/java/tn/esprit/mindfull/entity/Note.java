package tn.esprit.mindfull.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;


@Getter
@Setter
@Entity
public class Note {

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

    @Column(length = 1000)
    private String notes; // Visible only to doctor

    @Column(length = 1000)
    private String guidance;

    private LocalDate creationDate;
    private LocalDate updateDate;
    private LocalDate expirationDate;

    // Getters and Setters
}
