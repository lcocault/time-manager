package com.olicard.timemanager.interfaces.dto;

import com.olicard.timemanager.application.dto.TaskDTO;
import com.olicard.timemanager.domain.model.Importance;
import com.olicard.timemanager.domain.model.TaskType;
import com.olicard.timemanager.domain.model.Urgency;
import java.time.LocalDateTime;

public record TaskResponse(
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
    public static TaskResponse from(TaskDTO task) {
        return new TaskResponse(
                task.id(),
                task.title(),
                task.description(),
                task.deadline(),
                task.estimatedMinutes(),
                task.importance(),
                task.urgency(),
                task.type(),
                task.completed(),
                task.priorityScore()
        );
    }
}
