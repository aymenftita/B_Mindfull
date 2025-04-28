package tn.esprit.mindfull.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Feedback {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long feedbackId;

    private int rating; // 1-5
    private String comment;
    private String diagnostic;

    @ManyToOne
    @JoinColumn(name = "patient_id")
    private User patient;

    @ManyToOne
    @JoinColumn(name = "coach_id")
    private User coach; // coach qui reçoit

    @ManyToOne
    @JoinColumn(name = "content_id")
    private ProgramContent programContent;

    private LocalDateTime createdAt = LocalDateTime.now();

    // Méthodes correctement typées
    public Long getPatientId() {
        return patient != null ? patient.getId() : null;
    }

    public Long getCoachId() {
        return coach != null ? coach.getId() : null;
    }

    public Long getContentId() {
        return programContent != null ? programContent.getContentId() : null;
    }
}