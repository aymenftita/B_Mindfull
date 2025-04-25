package tn.esprit.mindfull.entity.Quizz_Test_Game;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;


// QuizzQuestion.java
@Entity
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuizzQuestion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long qq_id;

    @Nullable
    private boolean isText;

    private String questionText;

    @ManyToOne
    @JsonBackReference  // Prevent serialization of the entire Quizz
    private Quizz quizz;

    @OneToMany(mappedBy = "quizzQuestion", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Choice> choices;
}


