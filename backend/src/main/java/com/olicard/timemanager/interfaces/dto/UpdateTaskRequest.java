package com.olicard.timemanager.interfaces.dto;

import com.olicard.timemanager.application.dto.UpdateTaskCommand;
import com.olicard.timemanager.domain.model.Importance;
import com.olicard.timemanager.domain.model.TaskType;
import com.olicard.timemanager.domain.model.Urgency;
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
