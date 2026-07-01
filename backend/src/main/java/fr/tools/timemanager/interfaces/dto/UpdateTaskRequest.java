package fr.tools.timemanager.interfaces.dto;

import fr.tools.timemanager.application.dto.UpdateTaskCommand;
import fr.tools.timemanager.domain.model.Importance;
import fr.tools.timemanager.domain.model.TaskType;
import fr.tools.timemanager.domain.model.Urgency;
import jakarta.validation.constraints.Positive;
import java.time.LocalDateTime;

public record UpdateTaskRequest(
        String title,
        String description,
        LocalDateTime deadline,
        @Positive Integer estimatedMinutes,
        Importance importance,
        Urgency urgency,
        TaskType type,
        Boolean completed
) {
    public UpdateTaskCommand toCommand() {
        return new UpdateTaskCommand(title, description, deadline, estimatedMinutes, importance, urgency, type, completed);
    }
}
