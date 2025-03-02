package tn.esprit.mindfull.entity.Quizz_Test_Game;

import jakarta.persistence.*;

@Entity
@Table
public class Quizz {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long quizz_id;

    public void setQuizz_id(Long quizzId) {
        this.quizz_id = quizzId;
    }

    public Long getQuizz_id() {
        return quizz_id;
    }
}
