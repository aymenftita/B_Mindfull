package tn.esprit.mindfull.entity.Quizz_Test_Game;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.*;
import tn.esprit.mindfull.entity.User.User;
import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Score {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int score_id;

    private Date date;

    @Nullable
    private String name;

    @Nullable
    private String result;

    private String score_type;

    @ManyToOne
    @JoinColumn(name="id")
    private User user;

    @Override
    public String toString() {
        return "Score{" +
                "score_id=" + score_id +
                ", date=" + date +
                ", name='" + name + '\'' +
                ", result='" + result + '\'' +
                ", score_type='" + score_type + '\'' +
                ", user=" + user +
                '}';
    }
}
