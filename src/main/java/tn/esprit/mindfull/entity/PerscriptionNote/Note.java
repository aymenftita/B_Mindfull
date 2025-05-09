package tn.esprit.mindfull.entity.PerscriptionNote;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import tn.esprit.mindfull.entity.User.User;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coash_id")
    private User coach;

    @ManyToOne(fetch = FetchType.LAZY)
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

    @Override
    public String toString() {
        return "Note{" +
                "id=" + id +
                ", doctor=" + doctor +
                ", coach=" + coach +
                ", patient=" + patient +
                ", authorName='" + authorName + '\'' +
                ", diagnosis='" + diagnosis + '\'' +
                ", notes='" + notes + '\'' +
                ", guidance='" + guidance + '\'' +
                ", creationDate=" + creationDate +
                ", updateDate=" + updateDate +
                ", expirationDate=" + expirationDate +
                '}';
    }

    private LocalDate expirationDate;

    // Getters and Setters
}
