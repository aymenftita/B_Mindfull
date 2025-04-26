package tn.esprit.mindfull.dto.Appointmentsdto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WebRTCMessage {
    private String from;
    private String to;
    private String type; // "offer", "answer", "ice-candidate", "leave"
    private Object payload;
    private Integer appointmentId;
}