package com.olicard.timemanager.application.dto;

import com.olicard.timemanager.domain.model.Importance;
import com.olicard.timemanager.domain.model.TaskType;
import com.olicard.timemanager.domain.model.Urgency;
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
