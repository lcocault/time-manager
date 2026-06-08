package com.olicard.timemanager.application.dto;

import com.olicard.timemanager.domain.model.TaskType;
import java.time.LocalDateTime;

public record PlanningSlotDTO(
        LocalDateTime start,
        LocalDateTime end,
        TaskType type,
        Long taskId,
        String taskTitle,
        Long allocatedMinutes
) {
}
