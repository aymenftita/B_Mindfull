package tn.esprit.mindfull.Repository.AppointmentsRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tn.esprit.mindfull.entity.Appointment.Appointment;
import tn.esprit.mindfull.entity.Appointment.AppointmentStatus;
import tn.esprit.mindfull.entity.Appointment.Calendar;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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

    List<Appointment> findByPatientUserIdOrderByStartTimeDesc(Integer patientUserId);

    List<Appointment> findByPatientUserIdAndStartTimeAfterAndStatusNotOrderByStartTimeAsc(
            Integer patientUserId,
            LocalDateTime startTime,
            AppointmentStatus status);

    List<Appointment> findByPatientUserIdAndStartTimeBeforeOrderByStartTimeDesc(
            Integer patientUserId,
            LocalDateTime startTime);

    List<Appointment> findByCalendar_CalendarIdAndStartTimeBetweenAndStatusNot(
            Integer calendarId,
            LocalDateTime startTime,
            LocalDateTime endTime,
            AppointmentStatus status);

    Page<Appointment> findAll(Pageable pageable);

}
