package tn.esprit.mindfull.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

import java.util.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProgramContent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long contentId;

    private String title;
    private String contentType;
    private String contentDesc;
    private String mediaLink;

    @ManyToOne
    @JoinColumn(name = "program_id")
    @JsonIgnore
    private CoachingProgram program;
    @ManyToOne
    private CoachingProgram coachingProgram;
    // facultatif, mais utile pour recevoir juste l’ID dans le JSON
    @Transient
    private Long programId;
    /* @ManyToOne
    @JoinColumn(name = "program_id")
  @JsonBackReference(value = "program-content")
    private CoachingProgram coachingProgram;*/

    private Boolean completed;  // This is the 'completed' field that the query expects

    @Version
    private Long version;
    @ManyToMany
    @JoinTable(
            name = "program_content_user",  // nom de la table de jointure
            joinColumns = @JoinColumn(name = "content_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))

    private Set<User> users ;


    @ManyToOne
    private User user;

    // --- NOUVEAU getter pour exposer programId dans le JSON ---
    @JsonProperty("programId")
    public Long getProgramId() {
        return program != null ? program.getProgramId() : null;
    }

    // --- NOUVEAU setter pour lier programId entrant ---
    @JsonProperty("programId")
    public void setProgramId(Long programId) {
        if (this.program == null) {
            this.program = new CoachingProgram();
        }
        this.program.setProgramId(programId);
    }

    // Assurez-vous que l'utilisateur est bien défini pendant la désérialisation ou lors de la définition de l'objet
    @JsonProperty("user_id")
    public Long getUserId() {
        return user != null ? user.getUserId() : null;
    }
    // Définir l'utilisateur par user_id (utilisé pour la désérialisation ou la définition de l'utilisateur)
    @JsonProperty("user_id")
    public void setUserId(Long userId) {
        if (this.user == null) {
            this.user = new User(); // Assurez-vous que l'utilisateur est instancié
        }
        this.user.setUserId(userId); // Définit userId dans l'entité User
    }
    public Long getContentId() {
        return contentId;
    }

    public void setContentId(Long contentId) {
        this.contentId = contentId;
    }

    public Object getUsers() {

        return null;
    }
    public void setProgram(CoachingProgram program) {
        this.program = program;
    }




    @JsonProperty("contentDesc")
    public String getContentDesc() {
        return contentDesc;
    }

    @JsonProperty("contentDesc")
    public void setContentDesc(String contentDesc) {
        this.contentDesc = contentDesc;
    }



}
