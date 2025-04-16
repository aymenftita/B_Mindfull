package tn.esprit.mindfull.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

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
    @JsonBackReference(value = "program-content")
    private CoachingProgram coachingProgram;
    @Version
    private long version;
    // Ajoutez ces méthodes pour gérer la sérialisation/désérialisation
    @JsonProperty("coachingProgram")
    public void setCoachingProgramFromId(Map<String, Long> programMap) {
        if (programMap != null && programMap.containsKey("programId")) {
            this.coachingProgram = new CoachingProgram();
            this.coachingProgram.setProgramId(programMap.get("programId"));
        }
    }

    @JsonProperty("coachingProgram")
    public Map<String, Long> getCoachingProgramAsId() {
        if (this.coachingProgram != null) {
            Map<String, Long> map = new HashMap<>();
            map.put("programId", this.coachingProgram.getProgramId());
            return map;
        }
        return null;
    }
}
