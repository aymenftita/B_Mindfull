package tn.esprit.mindfull.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CoachingProgram {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long programId;
        // Relation avec le coach
        //@ManyToOne
        // @JoinColumn(name = "coach_id")
        // private User coach;
   // private Long coachId; // ID du coach associé
    private String title;
    private String description;
    @Temporal(TemporalType.DATE)
    private Date startDate;
    private Date endDate;
    private int participants;

    @OneToMany(mappedBy = "coachingProgram", cascade = CascadeType.ALL)
    //@JsonManagedReference(value = "program-content")
    @JsonIgnore // Ajoutez cette annotation pour ignorer la sérialisation
    private List<ProgramContent> contents;


    @ManyToOne
    @JoinColumn(name = "coach_id")
    @JsonBackReference
    private Coach coach;  // Ajoute cet attribut


    @Version
    private Long version;

}
