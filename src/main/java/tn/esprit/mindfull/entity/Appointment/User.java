package tn.esprit.mindfull.entity.Appointment;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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


}