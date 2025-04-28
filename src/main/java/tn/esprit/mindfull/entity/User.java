package tn.esprit.mindfull.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity(name = "user")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User {
    @Enumerated(EnumType.STRING)
    private Role role; // Correct: uses your custom enum
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    private String fullName;
    private String email;




    public void setId(Long id) {
        this.userId = id;
    }

    // Getter pour id
    public Long getId() {
        return userId;
    }

    @OneToMany(mappedBy = "user")
    @JsonManagedReference
    private List<CoachingProgram> coachingProgram;
    @ManyToMany(mappedBy = "users")
    private Set<ProgramContent> programContents ;




    public Long getUserId() {

        return this.userId;
    }

    public void setUserId(Long userId) {
    }
}
