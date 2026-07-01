package fr.tools.timemanager.application.dto;

import fr.tools.timemanager.domain.model.Importance;
import fr.tools.timemanager.domain.model.TaskType;
import fr.tools.timemanager.domain.model.Urgency;
import java.time.LocalDateTime;

public record CreateTaskCommand(
        String title,
        String description,
        LocalDateTime deadline,
        Integer estimatedMinutes,
        Importance importance,
        Urgency urgency,
        TaskType type
) {
}
