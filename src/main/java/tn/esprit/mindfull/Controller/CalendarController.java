package tn.esprit.mindfull.Controller;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.mindfull.Service.CalendarService;
import tn.esprit.mindfull.entity.Appointment.Appointment;
import tn.esprit.mindfull.entity.Appointment.Calendar;
import tn.esprit.mindfull.exception.ResourceNotFoundException;

import java.util.List;
import java.util.Map;


@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/calendars")
@Transactional
public class CalendarController {

    private final CalendarService calendarService;
    private static final Logger logger = LoggerFactory.getLogger(CalendarController.class);

    @Autowired
    public CalendarController(CalendarService calendarService) {
        this.calendarService = calendarService;
    }

    @PostMapping
    public ResponseEntity<?> createCalendar(@RequestBody Calendar calendar) {
        try {
            Calendar createdCalendar = calendarService.createCalendar(calendar);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdCalendar);
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<Calendar>> getAllCalendars() {
        return ResponseEntity.ok(calendarService.getAllCalendars());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCalendarById(@PathVariable Integer id) {
        Calendar calendar = calendarService.getCalendarById(id);  // Calls the service method
        return ResponseEntity.ok(calendar);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCalendar(@PathVariable Integer id, @RequestBody Calendar calendar) {
        try {
            Calendar updatedCalendar = calendarService.updateCalendar(id, calendar);
            return ResponseEntity.ok(updatedCalendar);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCalendar(@PathVariable Integer id) {
        try {
            calendarService.deleteCalendar(id);
            return ResponseEntity.noContent().build();
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.notFound().build();
        } catch (DataIntegrityViolationException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of(
                            "error", "Cannot delete calendar due to existing references",
                            "details", ex.getMostSpecificCause().getMessage()
                    ));
        } catch (Exception ex) {
            return ResponseEntity.internalServerError()
                    .body(Map.of(
                            "error", "Internal server error",
                            "message", "Failed to delete calendar"
                    ));
        }
    }
    }