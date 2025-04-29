package tn.esprit.mindfull.entity.Group;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@Table(name = "coaching_program")


public class Program {
    @Id
    private Long programId;
    private Long coachId;
    private String description;
    private LocalDateTime endDate;
    private Integer participants;
    private LocalDateTime startDate;
    private String title;
    private Long version;

    public Program() {}

    public Program(Long programId, Long coachId, String description, LocalDateTime endDate, Integer participants,
                   LocalDateTime startDate, String title, Long version) {
        this.programId = programId;
        this.coachId = coachId;
        this.description = description;
        this.endDate = endDate;
        this.participants = participants;
        this.startDate = startDate;
        this.title = title;
        this.version = version;
    }

}