package com.olicard.timemanager.domain.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Duration;
import org.junit.jupiter.api.Test;

class TaskTest {
    @Test
    void shouldComputeExtendedEisenhowerPriorityScore() {
        Task task = new Task(1L, "Préparer la présentation", "", null, Duration.ofMinutes(90), Importance.HIGH, Urgency.MEDIUM, TaskType.WORK, false);

        assertEquals(6, task.priorityScore());
    }

    @Test
    void shouldDefaultEstimatedTimeToThirtyMinutes() {
        Task task = new Task(1L, "Course rapide", "", null, Duration.ZERO, Importance.LOW, Urgency.LOW, TaskType.PERSONAL, false);

        assertEquals(30, task.estimatedTime().toMinutes());
    }
}
