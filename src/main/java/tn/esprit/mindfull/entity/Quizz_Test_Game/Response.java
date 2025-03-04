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
public class Response {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long response_id;

    private String answer;

    @OneToOne
    @JoinColumn(name = "test_question_id", nullable = false)
    private TestQuestion testQuestion;
}
