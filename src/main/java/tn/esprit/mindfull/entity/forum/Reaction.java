package tn.esprit.mindfull.entity.forum;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Reaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private ReactionType type;

    private LocalDateTime cteationTime = LocalDateTime.now();

    /*
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
    */

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;
}
