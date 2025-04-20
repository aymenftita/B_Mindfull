package tn.esprit.mindfull.entity.forum;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import tn.esprit.mindfull.user.User;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(length = 10000)
    private String content;

    @Column(nullable = false)
    private String tag;

    private LocalDateTime creationTime = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "author_id")
    private User author;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Comment> comments;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Reaction> reactions;

    @Column(nullable = false)
    private int viewCount = 0;

    public int getTotalReplies() {
        return comments != null ? comments.size() : 0;
    }

    // Add a method to get the total reactions count
    public int getTotalReactions() {
        if (reactions == null) {
            return 0;
        }

        // Calculate total reactions by summing the counts for each reaction type
        return reactions.stream()
                .mapToInt(r -> 1)  // count each reaction once
                .sum();
    }

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Report> reports;

}
