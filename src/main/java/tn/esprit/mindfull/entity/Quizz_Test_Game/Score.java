package tn.esprit.mindfull.entity.Quizz_Test_Game;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tn.esprit.mindfull.User;

import java.util.Date;

@Data
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
    private Long quizz_id;

    @Nullable
    private Long test_id;

    @Nullable
    private Long Game_id;

    private ScoreType score;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;




}
