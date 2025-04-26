package tn.esprit.mindfull.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Medication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String medicationName;

    @Column(length = 1000)
    private String directions;

    private int duration;

    @ManyToOne
    @JsonIgnore
    private Prescription prescription;
}
