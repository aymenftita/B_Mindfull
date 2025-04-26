package tn.esprit.mindfull.Repository.AppointmentsRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tn.esprit.mindfull.entity.Appointment.Calendar;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface CalendarRepository extends JpaRepository<Calendar, Integer> {

    // Full calendar fetch with all relationships
    @Query("SELECT c FROM Calendar c " +
            "LEFT JOIN FETCH c.owner " +
            "LEFT JOIN FETCH c.appointments " +
            "LEFT JOIN FETCH c.events " +
            "WHERE c.calendarId = :id")
    Optional<Calendar> findCalendarWithOwner(@Param("id") Integer id);


    // Owner-focused fetch
    @Query("SELECT c FROM Calendar c " +
            "LEFT JOIN FETCH c.owner " +
            "WHERE c.owner.userId = :userId")
    Optional<Calendar> findByOwnerUserId(@Param("userId") Integer userId);

    // Professional appointments with calendar data
    @Query("SELECT c FROM Calendar c " +
            "JOIN FETCH c.appointments a " +
            "JOIN FETCH a.professional " +
            "WHERE a.professional.userId = :professionalId " +
            "AND a.startTime BETWEEN :start AND :end")
    List<Calendar> findProfessionalCalendarsWithAppointments(
            @Param("professionalId") Integer professionalId,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );

    // Exists check with index optimization
    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END " +
            "FROM Calendar c WHERE c.owner.userId = :userId")
    boolean existsByOwnerUserId(@Param("userId") Integer userId);

    @Query("SELECT DISTINCT c FROM Calendar c LEFT JOIN FETCH c.appointments")
    List<Calendar> findAllWithAppointments();

    @Modifying
    @Query("DELETE FROM Calendar c WHERE c.calendarId = :id")
    void deleteByCalendarId(@Param("id") Integer id);

    Page<Calendar> findAll(Pageable pageable);


}