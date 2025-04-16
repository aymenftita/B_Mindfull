package tn.esprit.mindfull.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Coach {
    @jakarta.persistence.Id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long coach_id;

    private String name;

    public Coach(String coachX, int i) {
    }
    @OneToMany(mappedBy = "coach")
    private List<CoachingProgram> coachingPrograms;
    //  @ManyToOne
   // @JoinColumn(name = "coach_id")
    //private Coach coach;


}
