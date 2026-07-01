package fr.tools.timemanager.domain.port;

import fr.tools.timemanager.domain.model.TimeSlot;
import java.time.LocalDate;
import java.util.List;

public interface CalendarSyncPort {
    List<TimeSlot> getAvailableSlots(LocalDate startDate, int days);
}
