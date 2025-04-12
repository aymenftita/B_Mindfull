package tn.esprit.mindfull.Service;

import jakarta.persistence.Cacheable;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.mindfull.Repository.AppointmentRepository.AppointmentRepository; // Fixed package name
import tn.esprit.mindfull.Repository.AppointmentRepository.CalendarRepository;
import tn.esprit.mindfull.Repository.AppointmentRepository.UserRepository;
import tn.esprit.mindfull.entity.Appointment.*;
import tn.esprit.mindfull.exception.ResourceNotFoundException;

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

    @Autowired
    public AppointmentServiceImpl(AppointmentRepository appointmentRepository,
                                  UserRepository userRepository,
                                  CalendarRepository calendarRepository) {
        this.appointmentRepository = appointmentRepository;
        this.userRepository = userRepository;
        this.calendarRepository = calendarRepository;
    }

    @Override
    public Appointment createAppointment(Appointment appointment) {
        // Fetch and override patient with fully loaded entity
        User patient = userRepository.findById(appointment.getPatient().getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found"));
        validateUserRole(patient, UserRole.PATIENT, "Patient must have role PATIENT");
        appointment.setPatient(patient); // Critical fix

        // Fetch and override professional with fully loaded entity
        User professional = userRepository.findById(appointment.getProfessional().getUserId())
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







    private void validateUserRole(User user, UserRole requiredRole, String errorMessage) {
        if (user.getRole() != requiredRole) {
            throw new IllegalStateException(errorMessage);
        }
    }

    private void validateProfessionalRole(User professional) {
        if (!Set.of(UserRole.DOCTOR, UserRole.COACH).contains(professional.getRole())) {
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
            User newPatient = (User) userRepository.findById(updated.getPatient().getUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("Patient not found"));
            validateUserRole(newPatient, UserRole.PATIENT, "Invalid patient role");
            existing.setPatient(newPatient);
        }
    }

    private void updateProfessionalIfChanged(Appointment updated, Appointment existing) {
        if (updated.getProfessional() != null) {
            User newProfessional = (User) userRepository.findById(updated.getProfessional().getUserId())
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
}