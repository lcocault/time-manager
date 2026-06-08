package com.olicard.timemanager.domain.port;

import com.olicard.timemanager.domain.model.TimeSlot;
import java.time.LocalDate;
import java.util.List;

public interface CalendarSyncPort {
    List<TimeSlot> getAvailableSlots(LocalDate startDate, int days);
}
