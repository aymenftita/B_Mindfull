package tn.esprit.mindfull.Repository.AppointmentRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tn.esprit.mindfull.entity.Appointment.Appointment;
import tn.esprit.mindfull.entity.Appointment.AppointmentStatus;
import tn.esprit.mindfull.entity.Appointment.Calendar;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {


    void deleteByCalendar(Calendar calendar);
    List<Appointment> findByCalendarCalendarId(Integer calendarId);
    List<Appointment> findByStatus(AppointmentStatus status);
    List<Appointment> findByStartTimeBetween(LocalDateTime start, LocalDateTime end);
    @Query("SELECT a FROM Appointment a WHERE " +
            "a.calendar.calendarId = :calendarId AND " +
            "a.startTime < :endTime AND " +
            "a.endTime > :startTime")
    List<Appointment> findOverlappingAppointments(
            @Param("calendarId") Integer calendarId,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime
    );
    List<Appointment> findByPatientUserId(Integer patientId);
    List<Appointment> findByProfessionalUserId(Integer professionalId);

    @Query("SELECT a.status, COUNT(a) FROM Appointment a GROUP BY a.status")
    List<Object[]> countAppointmentsByStatus();

}
