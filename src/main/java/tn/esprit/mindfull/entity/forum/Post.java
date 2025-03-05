package tn.esprit.mindfull.entity.forum;

import jakarta.persistence.*;
import lombok.*;
import tn.esprit.mindfull.User;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long post_id;

    private String title;

    private String content;

    private String tag;

    private LocalDateTime created_at;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User author;


}
