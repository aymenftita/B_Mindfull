package tn.esprit.mindfull.entity.Quizz_Test_Game;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

// Choice.java
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Choice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long choice_id;

    private String text;
    private boolean correct;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "qq_id")
    private QuizzQuestion quizzQuestion;
}
