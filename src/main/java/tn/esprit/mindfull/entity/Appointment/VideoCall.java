package tn.esprit.mindfull.entity.Appointment;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tn.esprit.mindfull.entity.Appointment.Appointment;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VideoCall {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne
    @JoinColumn(name = "appointment_id")
    private Appointment appointment;

    private String roomId;
    private String accessLink;
    private String notes;

    @Enumerated(EnumType.STRING)
    private VideoStatus status;

    private LocalDateTime createdAt;
    private LocalDateTime startedAt;
    private LocalDateTime endedAt;

    // Constructor for creating a new video call
    public VideoCall(Appointment appointment) {
        this.appointment = appointment;
        this.status = VideoStatus.PENDING;
        this.createdAt = LocalDateTime.now();
    }
}