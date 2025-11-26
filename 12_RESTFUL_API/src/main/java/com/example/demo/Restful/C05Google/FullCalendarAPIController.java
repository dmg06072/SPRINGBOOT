package com.example.demo.Restful.C05Google;

import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.api.services.calendar.model.EventReminder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Date;

@Controller
@Slf4j
@RequestMapping("/google/cal")
public class FullCalendarAPIController {

    @GetMapping
    public void cal(){
        log.info("GET /google/cal...");
    }
    @PostMapping
    public String cal_post(
           @RequestParam("date") LocalDate date,
           @RequestParam("title") String title,
           @RequestParam("desc") String desc
    ){
        log.info("POST /google/cal...{},{},{}",date.toString(),title,desc);

        //붙여넣기!
        try {
            // Parse the date string to Date object
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date selectedDate = dateFormat.parse(date.toString());

            // Create a new event
            Event event = new Event()
                    .setSummary(title)
                    .setDescription(desc);

            // Set the start time to the selected date at 9:00 AM
            DateTime startDateTime = new DateTime(selectedDate);
            EventDateTime start = new EventDateTime()
                    .setDateTime(startDateTime)
                    .setTimeZone("Asia/Seoul");
            event.setStart(start);

            // Set the end time to the selected date at 10:00 AM
            DateTime endDateTime = new DateTime(new Date(selectedDate.getTime() + 3600000));
            EventDateTime end = new EventDateTime()
                    .setDateTime(endDateTime)
                    .setTimeZone("Asia/Seoul");
            event.setEnd(end);

            // Add reminder
            EventReminder[] reminderOverrides = new EventReminder[] {
                    new EventReminder().setMethod("email").setMinutes(24 * 60),
                    new EventReminder().setMethod("popup").setMinutes(10),
            };
            Event.Reminders reminders = new Event.Reminders()
                    .setUseDefault(false)
                    .setOverrides(Arrays.asList(reminderOverrides));
            event.setReminders(reminders);

            // Add the event to the calendar
            GoogleCalendar.addEvent(event);

        } catch (Exception e) {
            log.error("Error adding event to Google Calendar", e);
        }

        // Redirect to calendar view
        return "redirect:/google/cal";

    }
}
