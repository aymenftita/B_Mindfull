package tn.esprit.mindfull.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tn.esprit.mindfull.entity.Appointment.VideoStatus;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VideoRoomDTO {
    private String roomId;
    private Integer appointmentId;
    private VideoStatus status;
    private String patientId;
    private String professionalId;
}