package tn.esprit.mindfull.entity.forum;

import jakarta.persistence.*;
import lombok.*;
import tn.esprit.mindfull.entity.User.User;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String reason; // e.g., "Spam", "Abuse", "Offensive Content", etc.
    private LocalDateTime reportTime = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "reported_by_id")
    private User reportedBy;

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = true)
    private Post post;

    @ManyToOne
    @JoinColumn(name = "comment_id", nullable = true)
    private Comment comment;
}
