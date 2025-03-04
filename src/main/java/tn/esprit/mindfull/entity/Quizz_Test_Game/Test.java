package tn.esprit.mindfull.entity.Quizz_Test_Game;


import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Test {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long test_id;

    private String name;

    @OneToMany(mappedBy = "tq_id", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TestQuestion> testQuestions;

}
