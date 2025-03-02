package tn.esprit.mindfull.entity.appointment;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Appointment {
    @Id
    private Long appointment_id;


}
