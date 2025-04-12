package tn.esprit.mindfull.Service;

import tn.esprit.mindfull.entity.Appointment.Appointment;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface AppointmentService {
    Appointment createAppointment(Appointment appointment);
    List<Appointment> getAllAppointments();
    Appointment getAppointmentById(Integer id);
    Appointment updateAppointment(Integer id, Appointment appointment);
    void deleteAppointment(Integer id);
    Map<String, Long> getAppointmentStatistics();
    ByteArrayInputStream exportAppointmentsToExcel() throws IOException;
}
