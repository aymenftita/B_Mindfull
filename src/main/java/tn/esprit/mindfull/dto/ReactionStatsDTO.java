package tn.esprit.mindfull.dto;

public class ReactionStatsDTO {
    private String reactionType;
    private Integer count;
    private String postTitle;

    // Single unified constructor
    public ReactionStatsDTO(String label, Integer count, boolean isPost) {
        if (isPost) {
            this.postTitle = label;
        } else {
            this.reactionType = label;
        }
        this.count = count;
    }

    // Getters
    public String getReactionType() { return reactionType; }
    public String getPostTitle() { return postTitle; }
    public Integer getCount() { return count; }

    // Helper method for labels
    public String getLabel() {
        return postTitle != null ? postTitle : reactionType;
    }
}