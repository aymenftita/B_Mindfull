package tn.esprit.mindfull.entity.Quizz_Test_Game;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class QuestionResponse {
    @Id
    private Long id;



    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
