package com.olicard.timemanager.infrastructure.adapter;

import com.olicard.timemanager.domain.model.TaskType;
import com.olicard.timemanager.domain.model.TimeSlot;
import com.olicard.timemanager.domain.port.CalendarSyncPort;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class GoogleCalendarSyncAdapter implements CalendarSyncPort {
    @Override
    public List<TimeSlot> getAvailableSlots(LocalDate startDate, int days) {
        List<TimeSlot> slots = new ArrayList<>();
        for (int offset = 0; offset < days; offset++) {
            LocalDate day = startDate.plusDays(offset);
            slots.add(new TimeSlot(LocalDateTime.of(day, LocalTime.of(9, 0)), LocalDateTime.of(day, LocalTime.of(12, 0)), TaskType.WORK));
            slots.add(new TimeSlot(LocalDateTime.of(day, LocalTime.of(18, 0)), LocalDateTime.of(day, LocalTime.of(20, 0)), TaskType.PERSONAL));
        }
        return slots;
    }
}
