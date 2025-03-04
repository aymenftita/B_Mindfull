package tn.esprit.mindfull.entity.Quizz_Test_Game;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Choice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long choice_id;

    private String choiceText;

    private boolean isCorrect;

    @ManyToOne
    @JoinColumn(name = "qq_id")
    private QuizzQuestion quizzQuestion;




}
