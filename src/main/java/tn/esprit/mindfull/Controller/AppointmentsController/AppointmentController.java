package tn.esprit.mindfull.controller.AppointmentsController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.mindfull.Service.AppointmentsService.AppointmentService;
import tn.esprit.mindfull.entity.Appointment.Appointment;
import tn.esprit.mindfull.exception.ResourceNotFoundException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
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
    public ResponseEntity<?> getAllAppointments(
            @RequestParam(required = false, defaultValue = "false") boolean paginated,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {

        if (paginated) {
            // Return paginated results
            Page<Appointment> appointments = appointmentService.getAllAppointmentsPaginated(page, size);
            return ResponseEntity.ok(appointments);
        } else {
            // Return all appointments without pagination
            List<Appointment> appointments = appointmentService.getAllAppointments();
            return ResponseEntity.ok(appointments);
        }
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

    @GetMapping("/export/excel")
    public ResponseEntity<InputStreamResource> exportToExcel() {
        try {
            ByteArrayInputStream byteArrayInputStream = appointmentService.exportAppointmentsToExcel();

            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "attachment; filename=appointments.xlsx");

            return ResponseEntity
                    .ok()
                    .headers(headers)
                    .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
                    .body(new InputStreamResource(byteArrayInputStream));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PatchMapping("/{id}/reschedule")
    public ResponseEntity<?> rescheduleAppointment(
            @PathVariable Integer id,
            @RequestBody Map<String, String> timeData) {
        try {
            // Extract the new start and end times from the request body
            String startTimeStr = timeData.get("startTime");
            String endTimeStr = timeData.get("endTime");

            // Call the service method to update the appointment times
            Appointment updatedAppointment = appointmentService.rescheduleAppointment(id, startTimeStr, endTimeStr);

            return ResponseEntity.ok(updatedAppointment);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while rescheduling the appointment: " + e.getMessage());
        }
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<?> getAppointmentsByPatientId(@PathVariable Integer patientId) {
        try {
            List<Appointment> appointments = appointmentService.getAppointmentsByPatientId(patientId);
            return ResponseEntity.ok(appointments);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/patient/{patientId}/upcoming")
    public ResponseEntity<?> getUpcomingAppointmentsByPatientId(@PathVariable Integer patientId) {
        try {
            List<Appointment> appointments = appointmentService.getUpcomingAppointmentsByPatientId(patientId);
            return ResponseEntity.ok(appointments);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/patient/{patientId}/past")
    public ResponseEntity<?> getPastAppointmentsByPatientId(@PathVariable Integer patientId) {
        try {
            List<Appointment> appointments = appointmentService.getPastAppointmentsByPatientId(patientId);
            return ResponseEntity.ok(appointments);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/{appointmentId}/reschedule-request")
    public ResponseEntity<?> requestReschedule(
            @PathVariable Integer appointmentId,
            @RequestBody Map<String, String> requestData) {
        try {
            String startTimeStr = requestData.get("startTime");
            String endTimeStr = requestData.get("endTime");
            String reason = requestData.get("reason");

            if (startTimeStr == null || endTimeStr == null) {
                return ResponseEntity.badRequest().body("Start time and end time are required");
            }

            Appointment updatedAppointment = appointmentService.requestReschedule(
                    appointmentId, startTimeStr, endTimeStr, reason);

            return ResponseEntity.ok(updatedAppointment);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred: " + e.getMessage());
        }
    }

    @PutMapping("/{appointmentId}/cancel")
    public ResponseEntity<?> cancelAppointment(@PathVariable Integer appointmentId) {
        try {
            Appointment canceledAppointment = appointmentService.cancelAppointment(appointmentId);
            return ResponseEntity.ok(canceledAppointment);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred: " + e.getMessage());
        }
    }





}