package tn.esprit.mindfull.Service;

import tn.esprit.mindfull.entity.Appointment.Appointment;

import java.util.List;

public interface AppointmentService {
    Appointment createAppointment(Appointment appointment);
    List<Appointment> getAllAppointments();
    Appointment getAppointmentById(Integer id);
    Appointment updateAppointment(Integer id, Appointment appointment);
    void deleteAppointment(Integer id);
}
