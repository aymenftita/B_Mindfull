package tn.esprit.mindfull.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;
import java.util.Set;
import java.util.HashSet;

import java.util.HashMap;
import java.util.Map;

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
    private CoachingProgram program;


   // @ManyToOne
   // @JoinColumn(name = "program_id")
  //  @JsonBackReference(value = "program-content")
  //  private CoachingProgram coachingProgram;

    private Boolean completed;  // This is the 'completed' field that the query expects

    @Version
    private Long version;
    @ManyToMany
    @JoinTable(
            name = "program_content_user",  // nom de la table de jointure
            joinColumns = @JoinColumn(name = "content_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))

    private Set<User> users = new HashSet<>();


    @ManyToOne
    private User user;



    public Long getContentId() {
        return contentId;
    }

    public void setContentId(Long contentId) {
        this.contentId = contentId;
    }
}
