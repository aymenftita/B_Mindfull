package tn.esprit.mindfull.entity.forum;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tn.esprit.mindfull.user.User;


import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "reaction",
        uniqueConstraints = @UniqueConstraint(columnNames = {"post_id", "user_id"})
)
public class Reaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private ReactionType type;

    private LocalDateTime creationTime = LocalDateTime.now();

    /*
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
    */

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;


    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;


}
