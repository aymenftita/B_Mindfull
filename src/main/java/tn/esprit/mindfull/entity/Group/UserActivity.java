package tn.esprit.mindfull.entity.Group;

import jakarta.persistence.*;
import lombok.*;
import tn.esprit.mindfull.entity.User.User;

import java.time.LocalDateTime;
@Getter
@Setter
@Entity
@Table(name = "user_activity")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserActivity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String mood; // e.g., "Happy", "Sad", "Anxious"
    private Integer intensity; // e.g., 1 to 10
    private String notes; // Optional notes about the mood
    private LocalDateTime timestamp;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}