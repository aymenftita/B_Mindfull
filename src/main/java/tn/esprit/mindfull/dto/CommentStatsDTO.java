package tn.esprit.mindfull.dto;

public class CommentStatsDTO {
    private String period;
    private Long count;

    // Constructor
    public CommentStatsDTO(String period, Long count) {
        this.period = period;
        this.count = count;
    }

    // Getters & Setters
    public String getPeriod() { return period; }
    public void setPeriod(String period) { this.period = period; }
    public Long getCount() { return count; }
    public void setCount(Long count) { this.count = count; }
}