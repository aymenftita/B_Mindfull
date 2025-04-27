package tn.esprit.mindfull.user;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import tn.esprit.mindfull.entity.Appointment.Calendar;
import tn.esprit.mindfull.entity.Quizz_Test_Game.Score;

import java.util.Collection;

@Entity
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer userId;

    private String firstName;
    private String lastName;
    private String email;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Role is required") // Ensure role is never null
    private UserRole role;

    @OneToOne(mappedBy = "owner", cascade = CascadeType.ALL)
    @JsonBackReference
    @EqualsAndHashCode.Exclude  // Exclude from hashCode and equals
    @ToString.Exclude           // Exclude from toString
    private Calendar calendar;


    public String getName() {
        return this.firstName ;
    }

    public void setName(String name) {
        this.firstName = name;
    }

    public void setLastname(String devUser) {
        this.lastName = devUser;
    }

}