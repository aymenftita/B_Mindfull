package tn.esprit.mindfull.entity.Quizz_Test_Game;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
public class QuestionResponse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int qr_id;

    private String questionText;

    private String responseText;

    @ManyToOne
    @JoinColumn(name = "test", nullable = false)
    private Test test;

}
