package tn.esprit.mindfull.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProgramProgress {

    @jakarta.persistence.Id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long progress_id;

    @ManyToOne
    private CoachingProgram program;

    @ManyToOne
    private User user; // anciennement Client


    private int viewsCount;
    private double progressPercentage;
    private LocalDateTime lastViewedDate;

    public void setProgress_id(Long progressId) {
        this.progress_id = progressId;
    }

    public Long getProgress_id() {
        return progress_id;
    }

    public void setViewed(boolean b) {
    }
}

