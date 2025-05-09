package tn.esprit.mindfull.Service.AppointmentsService;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import tn.esprit.mindfull.Repository.AppointmentsRepository.AppointmentRepository; // Fixed package name
import tn.esprit.mindfull.Repository.AppointmentsRepository.CalendarRepository;
import tn.esprit.mindfull.Repository.UserRepository.UserRepository;
import tn.esprit.mindfull.entity.Appointment.*;
import tn.esprit.mindfull.exception.ResourceNotFoundException;
import tn.esprit.mindfull.entity.User.User;
import tn.esprit.mindfull.entity.User.Role;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Transactional
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final UserRepository userRepository;
    private final CalendarRepository calendarRepository;
    private final ExcelService excelService;

    @Autowired
    public AppointmentServiceImpl(AppointmentRepository appointmentRepository,
                                  UserRepository userRepository,
                                  CalendarRepository calendarRepository, ExcelService excelService) {
        this.appointmentRepository = appointmentRepository;
        this.userRepository = userRepository;
        this.calendarRepository = calendarRepository;
        this.excelService = excelService;
    }

    @Override
    public Appointment createAppointment(Appointment appointment) {
        // Fetch and override patient with fully loaded entity
        User patient = userRepository.findById(appointment.getPatient().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found"));
        validateUserRole(patient, Role.PATIENT, "Patient must have role PATIENT");
        appointment.setPatient(patient); // Critical fix

        // Fetch and override professional with fully loaded entity
        User professional = userRepository.findById(appointment.getProfessional().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Professional not found"));
        validateProfessionalRole(professional);
        appointment.setProfessional(professional); // Critical fix

        // Assign calendar if missing
        handleCalendarAssignment(appointment, professional);

        return appointmentRepository.save(appointment);
    }

    @Override
    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }

    @Override
    public Appointment getAppointmentById(Integer id) {
        return appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment with ID " + id + " not found"));
    }

    @Override
    public Appointment updateAppointment(Integer id, Appointment updatedAppointment) {
        return appointmentRepository.findById(id)
                .map(existing -> {
                    updatePatientIfChanged(updatedAppointment, existing);
                    updateProfessionalIfChanged(updatedAppointment, existing);
                    updateAppointmentDetails(updatedAppointment, existing);
                    return appointmentRepository.save(existing);
                })
                .orElseThrow(() -> new ResourceNotFoundException("Appointment with ID " + id + " not found"));
    }

    @Transactional
    public void deleteAppointment(Integer id) {
        if (appointmentRepository.existsById(id)) {
            appointmentRepository.deleteById(id);
            System.out.println("Appointment deletion attempted");
        } else {
            throw new ResourceNotFoundException("Appointment not found");
        }
    }

    private final Map<String, Map<String, Long>> appointmentStatsCache = new ConcurrentHashMap<>();

    @Override
    public Map<String, Long> getAppointmentStatistics() {
        // Use a fixed key or composite key if your caching depends on parameters.
        final String cacheKey = "appointmentStats";
        if (appointmentStatsCache.containsKey(cacheKey)) {
            return appointmentStatsCache.get(cacheKey);
        }

        List<Object[]> results = appointmentRepository.countAppointmentsByStatus();
        Map<String, Long> stats = new HashMap<>();
        for (Object[] result : results) {
            Object rawStatus = result[0];
            AppointmentStatus status;
            if (rawStatus instanceof Boolean) {
                status = ((Boolean) rawStatus) ? AppointmentStatus.SCHEDULED : AppointmentStatus.CANCELED;
            } else {
                String statusStr = rawStatus.toString();
                status = AppointmentStatus.valueOf(statusStr);
            }
            Long count = (Long) result[1];
            stats.put(status.name(), count);
        }
        appointmentStatsCache.put(cacheKey, stats);
        return stats;
    }


    @Override
    public ByteArrayInputStream exportAppointmentsToExcel() throws IOException {
        List<Appointment> appointments = appointmentRepository.findAll();
        return excelService.generateAppointmentsExcel(appointments);
    }


    private void validateUserRole(User user, Role requiredRole, String errorMessage) {
        if (user.getRole() != requiredRole) {
            throw new IllegalStateException(errorMessage);
        }
    }

    private void validateProfessionalRole(User professional) {
        if (!Set.of(Role.DOCTOR, Role.COACH).contains(professional.getRole())) {
            throw new IllegalStateException("Professional must be DOCTOR or COACH");
        }
    }

    private void handleCalendarAssignment(Appointment appointment, User professional) {
        if (appointment.getCalendar() == null) {
            Calendar professionalCalendar = professional.getCalendar();
            if (professionalCalendar == null) {
                throw new IllegalStateException("Professional has no associated calendar");
            }
            appointment.setCalendar(professionalCalendar);
        }
    }
    private void updatePatientIfChanged(Appointment updated, Appointment existing) {
        if (updated.getPatient() != null) {
            User newPatient = (User) userRepository.findById(updated.getPatient().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Patient not found"));
            validateUserRole(newPatient, Role.PATIENT, "Invalid patient role");
            existing.setPatient(newPatient);
        }
    }

    private void updateProfessionalIfChanged(Appointment updated, Appointment existing) {
        if (updated.getProfessional() != null) {
            User newProfessional = (User) userRepository.findById(updated.getProfessional().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Professional not found"));
            validateProfessionalRole(newProfessional);
            existing.setProfessional(newProfessional);
            existing.setCalendar(newProfessional.getCalendar());
        }
    }

    private void updateAppointmentDetails(Appointment updated, Appointment existing) {
        if (updated.getStartTime() != null) existing.setStartTime(updated.getStartTime());
        if (updated.getEndTime() != null) existing.setEndTime(updated.getEndTime());
        if (updated.getNotes() != null) existing.setNotes(updated.getNotes());
        if (updated.getStatus() != null) existing.setStatus(updated.getStatus());
        if (updated.getReminderTime() != null) existing.setReminderTime(updated.getReminderTime());
        if (updated.getReminderMessage() != null) existing.setReminderMessage(updated.getReminderMessage());
    }

    @Override
    public Appointment rescheduleAppointment(Integer id, String startTimeStr, String endTimeStr)
            throws ResourceNotFoundException, IllegalArgumentException {

        // Find the appointment by ID
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found with id: " + id));

        try {
            // Try different ISO format patterns to be more flexible
            LocalDateTime startTime;
            LocalDateTime endTime;

            try {
                // First try the default ISO format (most common)
                startTime = LocalDateTime.parse(startTimeStr);
                endTime = LocalDateTime.parse(endTimeStr);
            } catch (DateTimeParseException e) {
                // If that fails, try with a DateTimeFormatter
                DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
                startTime = LocalDateTime.parse(startTimeStr, formatter);
                endTime = LocalDateTime.parse(endTimeStr, formatter);
            }

            // Validate the time range
            if (endTime.isBefore(startTime)) {
                throw new IllegalArgumentException("End time cannot be before start time");
            }

            // Update the appointment times
            appointment.setStartTime(startTime);
            appointment.setEndTime(endTime);

            // Save and return the updated appointment
            return appointmentRepository.save(appointment);

        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date format. Use ISO format: yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        }
    }

    @Override
    public List<Appointment> getAppointmentsByPatientId(Long patientId) {
        if (patientId == null) {
            throw new IllegalArgumentException("Patient ID cannot be null");
        }
        User patient =userRepository.findById(patientId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + patientId));
        validateUserRole(patient, Role.PATIENT, "User must have role PATIENT");
        return appointmentRepository.findByPatientIdOrderByStartTimeDesc(patientId);
    }

    @Override
    public List<Appointment> getUpcomingAppointmentsByPatientId(Long patientId) {
        if (patientId == null) {
            throw new IllegalArgumentException("Patient ID cannot be null");
        }
        User patient = userRepository.findById(patientId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + patientId));
        validateUserRole(patient, Role.PATIENT, "User must have role PATIENT");
        LocalDateTime now = LocalDateTime.now();
        return appointmentRepository.findByPatientIdAndStartTimeAfterAndStatusNotOrderByStartTimeAsc(
                patientId, now, AppointmentStatus.CANCELED);
    }

    @Override
    public List<Appointment> getPastAppointmentsByPatientId(Long patientId) {
        if (patientId == null) {
            throw new IllegalArgumentException("Patient ID cannot be null");
        }
        User patient = userRepository.findById(patientId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + patientId));
        validateUserRole(patient, Role.PATIENT, "User must have role PATIENT");
        LocalDateTime now = LocalDateTime.now();
        return appointmentRepository.findByPatientIdAndStartTimeBeforeOrderByStartTimeDesc(patientId, now);
    }

    @Override
    public Appointment requestReschedule(Integer appointmentId, String startTimeStr, String endTimeStr, String reason) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found with id: " + appointmentId));

        // Convert string times to LocalDateTime
        LocalDateTime startTime;
        LocalDateTime endTime;

        try {
            try {
                // First try the default ISO format
                startTime = LocalDateTime.parse(startTimeStr);
                endTime = LocalDateTime.parse(endTimeStr);
            } catch (DateTimeParseException e) {
                // If that fails, try with a DateTimeFormatter
                DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
                startTime = LocalDateTime.parse(startTimeStr, formatter);
                endTime = LocalDateTime.parse(endTimeStr, formatter);
            }
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date format. Use ISO format: yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        }

        // Validate the times
        if (endTime.isBefore(startTime)) {
            throw new IllegalArgumentException("End time cannot be before start time");
        }

        // Store reschedule request info in the notes field
        String rescheduleNote = "Reschedule requested: " +
                "Current: " + appointment.getStartTime() + " to " + appointment.getEndTime() + ", " +
                "Proposed: " + startTime + " to " + endTime + ", " +
                "Reason: " + reason + ", " +
                "Requested at: " + LocalDateTime.now();

        // Append to existing notes if any
        if (appointment.getNotes() != null && !appointment.getNotes().isEmpty()) {
            appointment.setNotes(appointment.getNotes() + "\n\n" + rescheduleNote);
        } else {
            appointment.setNotes(rescheduleNote);
        }

        // Change status to indicate reschedule is pending
        appointment.setStatus(AppointmentStatus.RESCHEDULE_PENDING);

        // Save the updated appointment
        return appointmentRepository.save(appointment);
    }

    @Override
    public Appointment cancelAppointment(Integer appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found with id: " + appointmentId));

        // Check if the appointment can be canceled (e.g., not in the past)
        if (appointment.getStartTime().isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("Cannot cancel past appointments");
        }

        // Update the appointment status to CANCELED
        appointment.setStatus(AppointmentStatus.CANCELED);

        // Save and return the updated appointment
        return appointmentRepository.save(appointment);
    }
    @Override
    public Page<Appointment> getAllAppointmentsPaginated(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return appointmentRepository.findAll(pageable);
    }


}

