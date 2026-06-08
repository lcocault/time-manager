package com.olicard.timemanager.application.dto;

import com.olicard.timemanager.domain.model.Importance;
import com.olicard.timemanager.domain.model.TaskType;
import com.olicard.timemanager.domain.model.Urgency;
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
