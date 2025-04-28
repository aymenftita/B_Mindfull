package tn.esprit.mindfull.entity.Appointment;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import tn.esprit.mindfull.entity.User.User;
import tn.esprit.mindfull.entity.User.Role;
import tn.esprit.mindfull.validation.CreateValidation;
import tn.esprit.mindfull.validation.UpdateValidation;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "appointment")
@Getter
@Setter
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer appointmentId;

    @NotNull(groups = CreateValidation.class, message = "Start time is required")
    @Future(groups = {CreateValidation.class, UpdateValidation.class}, message = "Start time must be in the future")
    private LocalDateTime startTime;

    @NotNull(groups = CreateValidation.class, message = "End time is required")
    @Future(groups = {CreateValidation.class, UpdateValidation.class}, message = "End time must be after start time")
    private LocalDateTime endTime;

    private String notes;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Status is required")
    private AppointmentStatus status;

    @Enumerated(EnumType.STRING)
    private VideoStatus videoStatus;

    private LocalDateTime reminderTime;
    private String reminderMessage;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "calendar_id")
    @JsonBackReference
    @JsonIgnore
    private Calendar calendar;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id")
    private User patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "professional_id")
    private User professional;




    @PrePersist
    @PreUpdate
    private void validate() {
        validateRoles();
        validateTime();
        checkCalendar();
    }



        private void validateRoles() {
            // Ensure patient is not null and has the PATIENT role
            if (patient == null || patient.getRole() != Role.PATIENT) {
                throw new IllegalStateException("Invalid patient role");
            }

            // Ensure professional is a DOCTOR or COACH
            if (professional == null || !Set.of(Role.DOCTOR, Role.COACH).contains(professional.getRole())) {
                throw new IllegalStateException("Invalid professional role");
            }
        }

    private void validateTime() {
        if (startTime.isAfter(endTime)) {
            throw new IllegalStateException("End time must be after start time");
        }
    }

    private void checkCalendar() {
        if (calendar == null) {
            calendar = professional.getCalendar();
        }
    }
}