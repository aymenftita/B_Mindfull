package tn.esprit.mindfull.entity.Quizz_Test_Game;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.*;
import tn.esprit.mindfull.User;
import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Score {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int score_id;

    private Date date;

    @Nullable
    private String name;

    @Nullable
    private int result;

    private String score_type;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;




}
