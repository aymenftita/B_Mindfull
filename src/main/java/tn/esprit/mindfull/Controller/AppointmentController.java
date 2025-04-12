package tn.esprit.mindfull.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import tn.esprit.mindfull.Service.AppointmentService;
import tn.esprit.mindfull.entity.Appointment.Appointment;
import tn.esprit.mindfull.exception.ResourceNotFoundException;
import tn.esprit.mindfull.validation.CreateValidation;
import tn.esprit.mindfull.validation.UpdateValidation;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;

    @Autowired
    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @PostMapping
    public ResponseEntity<?> createAppointment(@RequestBody Appointment appointment) {
        try {
            Appointment createdAppointment = appointmentService.createAppointment(appointment);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdAppointment);
        } catch (IllegalStateException | ResourceNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<Appointment>> getAllAppointments() {
        List<Appointment> appointments = appointmentService.getAllAppointments();
        return ResponseEntity.ok(appointments);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getAppointmentById(@PathVariable Integer id) {
        try {
            Appointment appointment = appointmentService.getAppointmentById(id);
            return ResponseEntity.ok(appointment);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateAppointment(@PathVariable Integer id, @RequestBody Appointment appointment) {
        try {
            Appointment updatedAppointment = appointmentService.updateAppointment(id, appointment);
            return ResponseEntity.ok(updatedAppointment);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAppointment(@PathVariable Integer id) {
        try {
            appointmentService.deleteAppointment(id);
            return ResponseEntity.noContent().build(); // HTTP 204 No Content on success
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build(); // HTTP 404 Not Found if appointment doesn’t exist
        } catch (Exception e) {
            System.out.println("Error deleting appointment: " + e.getMessage());
            return ResponseEntity.status(500).build(); // HTTP 500 Internal Server Error on failure
        }
    }

    @GetMapping("/statistics")
    public ResponseEntity<Map<String, Long>> getAppointmentStatistics() {
        Map<String, Long> stats = appointmentService.getAppointmentStatistics();
        return ResponseEntity.ok(stats);
    }
}