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
public class TestQuestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tq_id;

    private String questionText;

    @ManyToOne
    @JoinColumn(name = "test", nullable = false)
    private Test test;

    @OneToOne(mappedBy = "testQuestion", cascade = CascadeType.ALL, orphanRemoval = true)
    private Response response;
}
