package tn.esprit.mindfull.entity.Quizz_Test_Game;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class Test {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long test_id;

    private String content;


    private void setTest_id(Long testId) {
        this.test_id = testId;
    }

}
