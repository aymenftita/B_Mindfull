package tn.esprit.mindfull.dto.Groupdto;

import lombok.Data;

@Data
public class MoodTrackerRequest {
    private Long userId;
    private String mood;
    private Integer intensity;
    private String notes;
}