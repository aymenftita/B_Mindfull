package tn.esprit.mindfull.entity.Quizz_Test_Game;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Quizz {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long quizz_id;

    private String title;

    @OneToMany(mappedBy = "quizz", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<QuizzQuestion> questions;


}
