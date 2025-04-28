package tn.esprit.mindfull.Dto;

import lombok.Data;

@Data
public class MoodTrackerRequest {
    private Long userId;
    private String mood;
    private Integer intensity;
    private String notes;
}