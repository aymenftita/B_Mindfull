package tn.esprit.mindfull.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
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

    private String title;
    private String description;

    @Temporal(TemporalType.DATE)
    private Date startDate;
    private Date endDate;

    private int participants;

    @OneToMany(mappedBy = "program", cascade = CascadeType.ALL)
    @JsonIgnore // Ignore la sérialisation des contenus
    private List<ProgramContent> contents;

    private LocalDateTime createdAt; // Date de création du programme

    @ManyToOne
    @JsonBackReference
@JoinColumn(name = "coach_id")  // Assure-toi que cette colonne existe dans la table `coaching_program`
    private User coach;

    public void setId(Long id) {
        this.programId = id;
    }

    public Long getId() {
        return programId;
    }

    // Méthode pour associer un coach au programme
    public void assignCoach(User coach) {
        this.coach = coach;
    }
}
