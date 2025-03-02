package tn.esprit.mindfull.entity.Quizz_Test_Game;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Choice
{

    @Id
    private Long choice_id;

    public void setChoice_id(Long choiceId) {
        this.choice_id = choiceId;
    }

    public Long getChoice_id() {
        return choice_id;
    }
}
