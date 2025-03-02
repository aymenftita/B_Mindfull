package tn.esprit.mindfull.entity.Quizz_Test_Game;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.Setter;

import java.util.List;

@Entity
@Data
public class Question {

    @Id
    private Long question_id;

    @OneToMany
    private List<Response> response;

}
