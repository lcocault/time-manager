package fr.tools.timemanager.domain.model;

import java.time.Duration;
import java.time.LocalDateTime;

public record Task(
        Long id,
        String title,
        String description,
        LocalDateTime deadline,
        Duration estimatedTime,
        Importance importance,
        Urgency urgency,
        TaskType type,
        boolean completed
) {
    public Task {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("title must not be blank");
        }
        description = description == null ? "" : description.trim();
        estimatedTime = estimatedTime == null || estimatedTime.isNegative() || estimatedTime.isZero()
                ? Duration.ofMinutes(30)
                : estimatedTime;
        importance = importance == null ? Importance.MEDIUM : importance;
        urgency = urgency == null ? Urgency.MEDIUM : urgency;
        type = type == null ? TaskType.WORK : type;
    }

    public int priorityScore() {
        return importance.weight() * urgency.weight();
    }
}
