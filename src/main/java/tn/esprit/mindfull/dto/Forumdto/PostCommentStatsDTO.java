package tn.esprit.mindfull.dto.Forumdto;

public class PostCommentStatsDTO {
    private String postTitle;
    private Long commentCount;

    // Constructors
    public PostCommentStatsDTO() {
        // Default constructor
    }

    public PostCommentStatsDTO(String postTitle, Long commentCount) {
        this.postTitle = postTitle;
        this.commentCount = commentCount;
    }

    // Getters and Setters
    public String getPostTitle() {
        return postTitle;
    }

    public void setPostTitle(String postTitle) {
        this.postTitle = postTitle;
    }

    public Long getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(Long commentCount) {
        this.commentCount = commentCount;
    }

    // toString() for debugging
    @Override
    public String toString() {
        return "PostCommentStatsDTO{" +
                "postTitle='" + postTitle + '\'' +
                ", commentCount=" + commentCount +
                '}';
    }
}