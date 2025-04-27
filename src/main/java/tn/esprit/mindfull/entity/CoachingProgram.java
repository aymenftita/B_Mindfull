package tn.esprit.mindfull.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
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



    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "user_id",nullable = false)  // Assure-toi que cette colonne existe dans la table `coaching_program`
    private User user;

    @Version
    private Long version;
    public void setId(Long id) {
        this.programId = id;
    }

    public Long getId() {
        return programId;
    }

    // --- NOUVEAU getter pour exposer coachId ---
    @JsonProperty("coachId")
    public Long getCoachId() {

        return (user != null) ? user.getId() : null;
    }


    // Méthode pour associer un coach au programme
    public void assignCoach(User coach) {
        this.user = coach;
    }


    public void setCoachId(Long coachId) {
    }

    public void setCoach(User coach) {
    }
}
