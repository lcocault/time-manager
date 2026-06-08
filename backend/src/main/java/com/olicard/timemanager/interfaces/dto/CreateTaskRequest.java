package com.olicard.timemanager.interfaces.dto;

import com.olicard.timemanager.application.dto.CreateTaskCommand;
import com.olicard.timemanager.domain.model.Importance;
import com.olicard.timemanager.domain.model.TaskType;
import com.olicard.timemanager.domain.model.Urgency;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import java.time.LocalDateTime;

public record CreateTaskRequest(
        @NotBlank String title,
        String description,
        LocalDateTime deadline,
        @Positive Integer estimatedMinutes,
        Importance importance,
        Urgency urgency,
        TaskType type
) {
    public CreateTaskCommand toCommand() {
        return new CreateTaskCommand(title, description, deadline, estimatedMinutes, importance, urgency, type);
    }
}
