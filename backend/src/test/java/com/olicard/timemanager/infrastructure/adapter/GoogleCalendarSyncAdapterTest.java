package com.olicard.timemanager.infrastructure.adapter;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.olicard.timemanager.domain.model.TaskType;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;

class GoogleCalendarSyncAdapterTest {
    private final GoogleCalendarSyncAdapter adapter = new GoogleCalendarSyncAdapter();

    @Test
    void shouldGenerateWorkAndPersonalSlotsForFiveDays() {
        var slots = adapter.getAvailableSlots(LocalDate.of(2026, 6, 8), 5);

        assertEquals(10, slots.size());
        assertEquals(TaskType.WORK, slots.get(0).type());
        assertEquals(TaskType.PERSONAL, slots.get(1).type());
    }
}
