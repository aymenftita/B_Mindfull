package tn.esprit.mindfull.Service.AppointmentsService;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import tn.esprit.mindfull.Repository.AppointmentsRepository.AppointmentRepository;
import tn.esprit.mindfull.Repository.AppointmentsRepository.CalendarRepository;
import tn.esprit.mindfull.Repository.UserRepository.UserRepository;
import tn.esprit.mindfull.entity.Appointment.*;
import tn.esprit.mindfull.entity.Appointment.Calendar;
import tn.esprit.mindfull.exception.ResourceNotFoundException;
import tn.esprit.mindfull.entity.User.User;
import tn.esprit.mindfull.entity.User.Role;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

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
        User owner = userRepository.findById(calendar.getOwner().getId())
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
                User newOwner = (User) userRepository.findById(updatedCalendar.getOwner().getId())
                        .orElseThrow(() -> new ResourceNotFoundException("Owner not found"));
                if (!Set.of(Role.DOCTOR, Role.COACH).contains(newOwner.getRole())) {
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

    @Override
    public List<Map<String, Object>> getAvailableTimeSlotsForPatient(Integer patientId) {
        // Verify patient exists
        User patient = userRepository.findById(Long.valueOf(patientId))
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + patientId));

        if (patient.getRole() != Role.PATIENT) {
            throw new IllegalStateException("User must have role PATIENT");
        }

        // Get all professional calendars (doctors and coaches)
        List<User> professionals = userRepository.findByRoleIn(Arrays.asList(Role.DOCTOR, Role.COACH));

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime twoWeeksFromNow = now.plusWeeks(2);

        List<Map<String, Object>> availableSlots = new ArrayList<>();

        // For each professional
        for (User professional : professionals) {
            Calendar calendar = professional.getCalendar();

            if (calendar == null) {
                continue;
            }

            // Get professional's existing appointments in the next two weeks
            List<Appointment> existingAppointments = appointmentRepository.findByCalendar_CalendarIdAndStartTimeBetweenAndStatusNot(
                    calendar.getCalendarId(), now, twoWeeksFromNow, AppointmentStatus.CANCELED);

            // Create a set of busy time slots
            Set<LocalDateTime> busyTimes = existingAppointments.stream()
                    .flatMap(appointment -> {
                        List<LocalDateTime> slots = new ArrayList<>();
                        LocalDateTime current = appointment.getStartTime();
                        while (current.isBefore(appointment.getEndTime())) {
                            slots.add(current);
                            current = current.plusMinutes(30); // Assuming 30-minute slot increments
                        }
                        return slots.stream();
                    })
                    .collect(Collectors.toSet());

            // For each day in the next two weeks
            for (int day = 0; day < 14; day++) {
                LocalDate date = now.plusDays(day).toLocalDate();

                // Skip weekends if needed (example - adjust as needed)
                if (date.getDayOfWeek().getValue() > 5) { // Skip Saturday and Sunday
                    continue;
                }

                // Get working hours for this professional (example - adjust as needed)
                // This should be retrieved from the calendar or professional settings
                LocalTime startHour = LocalTime.of(9, 0); // 9:00 AM
                LocalTime endHour = LocalTime.of(17, 0);  // 5:00 PM

                // Create slots for this day
                LocalDateTime slotStart = LocalDateTime.of(date, startHour);

                while (slotStart.toLocalTime().isBefore(endHour)) {
                    LocalDateTime slotEnd = slotStart.plusMinutes(30); // 30-minute slots

                    // Skip if this slot is in the past
                    if (slotStart.isBefore(now)) {
                        slotStart = slotEnd;
                        continue;
                    }

                    // Check if this slot is available
                    if (!busyTimes.contains(slotStart)) {
                        Map<String, Object> slot = new HashMap<>();
                        slot.put("professionalId", professional.getId());
                        slot.put("professionalName", professional.getFirstname() + " " + professional.getLastname());
                        slot.put("professionalRole", professional.getRole().toString());
                        slot.put("startTime", slotStart);
                        slot.put("endTime", slotEnd);
                        slot.put("calendarId", calendar.getCalendarId());
                        availableSlots.add(slot);
                    }

                    slotStart = slotEnd;
                }
            }
        }

        return availableSlots;
    }

    @Override
    public Page<Calendar> getAllCalendarsPaginated(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return calendarRepository.findAll(pageable);
    }




}
