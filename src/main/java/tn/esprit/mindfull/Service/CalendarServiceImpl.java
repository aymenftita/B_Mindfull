package tn.esprit.mindfull.Service;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.mindfull.Repository.AppointmentRepository.AppointmentRepository;
import tn.esprit.mindfull.Repository.AppointmentRepository.CalendarRepository;
import tn.esprit.mindfull.Repository.AppointmentRepository.UserRepository;
import tn.esprit.mindfull.entity.Appointment.Appointment;
import tn.esprit.mindfull.entity.Appointment.Calendar;
import tn.esprit.mindfull.entity.Appointment.User;
import tn.esprit.mindfull.entity.Appointment.UserRole;
import tn.esprit.mindfull.exception.ResourceNotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
public class CalendarServiceImpl implements CalendarService {

    @Autowired
    private final CalendarRepository calendarRepository;
    private final EntityManager entityManager;
    private final AppointmentRepository appointmentRepository;
    private final UserRepository userRepository;
    private static final Logger logger = LoggerFactory.getLogger(CalendarService.class);

    @Autowired
    public CalendarServiceImpl(CalendarRepository calendarRepository, EntityManager entityManager, AppointmentRepository appointmentRepository, UserRepository userRepository) {
        this.calendarRepository = calendarRepository;
        this.entityManager = entityManager;
        this.appointmentRepository = appointmentRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Calendar createCalendar(Calendar calendar) {
        // Fetch the user from the database
        User owner = userRepository.findById(calendar.getOwner().getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // Set the validated user as the owner
        calendar.setOwner(owner);
        return calendarRepository.save(calendar);
    }

    @Override
    public List<Calendar> getAllCalendars() {
        return calendarRepository.findAllWithAppointments();
    }




    @Override
    public Calendar getCalendarById(Integer id) {
        return calendarRepository.findCalendarWithOwner(id)
                .orElseThrow(() -> new ResourceNotFoundException("Calendar not found"));
    }

    @Transactional
    @Override
    public Calendar updateCalendar(Integer id, Calendar updatedCalendar) {
        return calendarRepository.findById(id).map(existing -> {
            // Update owner (if changed)
            if (updatedCalendar.getOwner() != null) {
                User newOwner = (User) userRepository.findById(updatedCalendar.getOwner().getUserId())
                        .orElseThrow(() -> new ResourceNotFoundException("Owner not found"));
                if (!Set.of(UserRole.DOCTOR, UserRole.COACH).contains(newOwner.getRole())) {
                    throw new IllegalStateException("Invalid owner role");
                }
                existing.setOwner(newOwner);
            }

            // Update events
            if (updatedCalendar.getEvents() != null) {
                existing.setEvents(updatedCalendar.getEvents());
            }

            return calendarRepository.save(existing);
        }).orElseThrow(() -> new ResourceNotFoundException("Calendar not found"));
    }




    @Transactional
    public void deleteCalendar(Integer id) {
        // Fetch the calendar with its relationships
        Calendar calendar = calendarRepository.findCalendarWithOwner(id)
                .orElseThrow(() -> new ResourceNotFoundException("Calendar not found"));

        // Break the association with the owner
        User owner = calendar.getOwner();
        if (owner != null) {
            owner.setCalendar(null);
            entityManager.merge(owner);
        }

        // Break bi-directional associations for appointments
        if (calendar.getAppointments() != null) {
            // Iterate over a copy to avoid ConcurrentModificationException
            for (Appointment appointment : List.copyOf(calendar.getAppointments())) {
                // Break the link from the appointment to the calendar.
                appointment.setCalendar(null);
                // Remove the appointment if they should be deleted
                entityManager.remove(appointment);
            }
            calendar.getAppointments().clear();
        }

        // Clear the events map (element collection)
        if (calendar.getEvents() != null) {
            calendar.getEvents().clear();
        }

        // Remove the calendar itself
        entityManager.remove(calendar);

        // Force changes to the database
        entityManager.flush();

        // Clear persistence context to ensure no stale entities remain
        entityManager.clear();
    }

}
