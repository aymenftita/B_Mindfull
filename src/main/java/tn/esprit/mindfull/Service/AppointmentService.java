package tn.esprit.mindfull.Service;

import tn.esprit.mindfull.entity.Appointment.Appointment;
import tn.esprit.mindfull.exception.ResourceNotFoundException;

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
    Appointment rescheduleAppointment(Integer id, String startTimeStr, String endTimeStr)
            throws ResourceNotFoundException, IllegalArgumentException;

    List<Appointment> getAppointmentsByPatientId(Integer patientId);

    List<Appointment> getUpcomingAppointmentsByPatientId(Integer patientId);

    List<Appointment> getPastAppointmentsByPatientId(Integer patientId);

    Appointment requestReschedule(Integer appointmentId, String startTimeStr, String endTimeStr, String reason);

    Appointment cancelAppointment(Integer appointmentId);
}
