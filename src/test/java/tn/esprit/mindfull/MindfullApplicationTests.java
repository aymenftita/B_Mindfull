package tn.esprit.mindfull;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import tn.esprit.mindfull.entity.Appointment.Appointment; // Fixed import
import tn.esprit.mindfull.entity.Appointment.AppointmentStatus;
import tn.esprit.mindfull.entity.Appointment.VideoStatus;
import java.time.LocalDateTime;

@SpringBootTest
class MindfullApplicationTests {

    @Test
    void contextLoads() {
        Appointment appointment = Appointment.builder()
                .startTime(LocalDateTime.now())
                .endTime(LocalDateTime.now().plusHours(1))
                .notes("Initial consultation")
                .status(AppointmentStatus.SCHEDULED)
                .videoStatus(VideoStatus.PENDING)
                .build();

        System.out.println(appointment);
    }
}