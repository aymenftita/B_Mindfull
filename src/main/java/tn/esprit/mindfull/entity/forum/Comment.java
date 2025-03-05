package tn.esprit.mindfull.entity.forum;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tn.esprit.mindfull.User;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long comment_id;

   private String comment;

   private LocalDateTime comment_date;

   @OneToOne
   @JoinColumn(name = "user_id")
    private User author;

   @ManyToOne
   @JoinColumn(name = "post_id")
   private Post post;

}
