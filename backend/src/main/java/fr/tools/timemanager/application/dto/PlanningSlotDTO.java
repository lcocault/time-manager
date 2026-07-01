package fr.tools.timemanager.application.dto;

import fr.tools.timemanager.domain.model.TaskType;
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
