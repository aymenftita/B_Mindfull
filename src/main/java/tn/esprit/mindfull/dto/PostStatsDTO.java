package tn.esprit.mindfull.dto;

import lombok.Data;

@Data
public class PostStatsDTO {
    private String label; // e.g., date or username
    private Long value;   // count of posts

    // Explicitly declare the constructor
    public PostStatsDTO(String label, Long value) {
        this.label = label;
        this.value = value;
    }
}