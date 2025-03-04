package tn.esprit.mindfull.entity.Quizz_Test_Game;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class QuizzQuestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long qq_id;

    private String questionText;

    @ManyToOne
    @JoinColumn(name = "quizz_id")
    private Quizz quizz;

    @OneToMany(mappedBy = "quizzQuestion", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Choice> choices;

}
