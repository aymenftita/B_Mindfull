package tn.esprit.mindfull.entity.Quizz_Test_Game;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;


@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Quizz {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long quizz_id;

    @Column(nullable = false)  // Make title mandatory
    private String title;

    private String description;

    private String image;

    @OneToMany(mappedBy = "quizz", cascade = CascadeType.ALL, orphanRemoval = true)
      // Add this to break the circular reference
    private List<QuizzQuestion> questions;
}



