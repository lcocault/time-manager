package com.olicard.timemanager.application.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.olicard.timemanager.application.dto.CreateTaskCommand;
import com.olicard.timemanager.application.dto.UpdateTaskCommand;
import com.olicard.timemanager.domain.model.Importance;
import com.olicard.timemanager.domain.model.Task;
import com.olicard.timemanager.domain.model.TaskType;
import com.olicard.timemanager.domain.model.Urgency;
import com.olicard.timemanager.domain.port.TaskRepositoryPort;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {
    @Mock
    private TaskRepositoryPort taskRepositoryPort;

    @InjectMocks
    private TaskService taskService;

    @Test
    void shouldCreateTaskWithPriorityScore() {
        when(taskRepositoryPort.save(any(Task.class))).thenAnswer(invocation -> {
            Task task = invocation.getArgument(0);
            return new Task(10L, task.title(), task.description(), task.deadline(), task.estimatedTime(), task.importance(), task.urgency(), task.type(), task.completed());
        });

        var createdTask = taskService.createTask(new CreateTaskCommand(
                "Bloquer la roadmap",
                "Préparer les sujets du sprint",
                LocalDateTime.of(2026, 6, 10, 9, 0),
                120,
                Importance.HIGH,
                Urgency.HIGH,
                TaskType.WORK
        ));

        assertEquals(10L, createdTask.id());
        assertEquals(9, createdTask.priorityScore());
        assertEquals(120, createdTask.estimatedMinutes());
    }

    @Test
    void shouldUpdateExistingTaskCompletion() {
        Task existingTask = new Task(1L, "Réviser budget", "", null, Duration.ofMinutes(45), Importance.MEDIUM, Urgency.MEDIUM, TaskType.PERSONAL, false);
        when(taskRepositoryPort.findById(1L)).thenReturn(Optional.of(existingTask));
        when(taskRepositoryPort.save(any(Task.class))).thenAnswer(invocation -> invocation.getArgument(0));

        var updatedTask = taskService.updateTask(1L, new UpdateTaskCommand(null, null, null, null, null, null, null, true));

        assertEquals(true, updatedTask.completed());
        verify(taskRepositoryPort).save(any(Task.class));
    }
}
