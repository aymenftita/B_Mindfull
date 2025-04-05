package tn.esprit.mindfull.Service;

import tn.esprit.mindfull.entity.Appointment.Calendar;

import java.util.List;

public interface CalendarService {
    Calendar createCalendar(Calendar calendar);
    List<Calendar> getAllCalendars();
    Calendar getCalendarById(Integer id);
    Calendar updateCalendar(Integer id, Calendar calendar);
    void deleteCalendar(Integer id);


}
