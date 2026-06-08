package com.olicard.timemanager.domain.model;

import java.time.Duration;
import java.time.LocalDateTime;

public record TimeSlot(LocalDateTime start, LocalDateTime end, TaskType type) {
    public TimeSlot {
        if (start == null || end == null || !end.isAfter(start)) {
            throw new IllegalArgumentException("A time slot must have an end after its start");
        }
        type = type == null ? TaskType.WORK : type;
    }

    public long durationMinutes() {
        return Duration.between(start, end).toMinutes();
    }
}
