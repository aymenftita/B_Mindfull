package tn.esprit.mindfull.Service;

import org.springframework.data.domain.Page;
import tn.esprit.mindfull.entity.Appointment.Appointment;
import tn.esprit.mindfull.entity.Appointment.Calendar;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface CalendarService {
    Calendar createCalendar(Calendar calendar);
    List<Calendar> getAllCalendars();
    Calendar getCalendarById(Integer id);
    Calendar updateCalendar(Integer id, Calendar calendar);
    void deleteCalendar(Integer id);
    List<Map<String, Object>> getAvailableTimeSlotsForPatient(Integer patientId);

    Page<Calendar> getAllCalendarsPaginated(int page, int size);




}
