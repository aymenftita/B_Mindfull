package tn.esprit.mindfull.entity.Appointment;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@DiscriminatorValue("TELEMEDICINE")
@NoArgsConstructor
public class Telemedicine extends Appointment {
    private String videoRoomId;
    private String consultationLink;
}
