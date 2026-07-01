package fr.tools.timemanager.application.dto;

import fr.tools.timemanager.domain.model.Importance;
import fr.tools.timemanager.domain.model.TaskType;
import fr.tools.timemanager.domain.model.Urgency;
import java.time.LocalDateTime;

public record TaskDTO(
        Long id,
        String title,
        String description,
        LocalDateTime deadline,
        long estimatedMinutes,
        Importance importance,
        Urgency urgency,
        TaskType type,
        boolean completed,
        int priorityScore
) {
}
