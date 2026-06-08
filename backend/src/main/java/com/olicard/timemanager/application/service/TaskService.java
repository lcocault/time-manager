package com.olicard.timemanager.application.service;

import com.olicard.timemanager.application.dto.CreateTaskCommand;
import com.olicard.timemanager.application.dto.TaskDTO;
import com.olicard.timemanager.application.dto.UpdateTaskCommand;
import com.olicard.timemanager.domain.exception.TaskNotFoundException;
import com.olicard.timemanager.domain.model.Importance;
import com.olicard.timemanager.domain.model.Task;
import com.olicard.timemanager.domain.model.TaskType;
import com.olicard.timemanager.domain.model.Urgency;
import com.olicard.timemanager.domain.port.TaskRepositoryPort;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class TaskService {
    private final TaskRepositoryPort taskRepositoryPort;

    public TaskService(TaskRepositoryPort taskRepositoryPort) {
        this.taskRepositoryPort = taskRepositoryPort;
    }

    public TaskDTO createTask(CreateTaskCommand command) {
        Task task = new Task(
                null,
                command.title(),
                command.description(),
                command.deadline(),
                toDuration(command.estimatedMinutes()),
                valueOrDefault(command.importance(), Importance.MEDIUM),
                valueOrDefault(command.urgency(), Urgency.MEDIUM),
                valueOrDefault(command.type(), TaskType.WORK),
                false
        );
        return toDto(taskRepositoryPort.save(task));
    }

    public List<TaskDTO> listTasks() {
        return taskRepositoryPort.findAll().stream()
                .sorted(Comparator.comparingInt(Task::priorityScore)
                        .reversed()
                        .thenComparing(task -> task.deadline() == null ? LocalDateTime.MAX : task.deadline()))
                .map(this::toDto)
                .toList();
    }

    public TaskDTO updateTask(Long taskId, UpdateTaskCommand command) {
        Task existing = taskRepositoryPort.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException(taskId));

        Task updated = new Task(
                existing.id(),
                valueOrDefault(command.title(), existing.title()),
                valueOrDefault(command.description(), existing.description()),
                valueOrDefault(command.deadline(), existing.deadline()),
                command.estimatedMinutes() == null ? existing.estimatedTime() : toDuration(command.estimatedMinutes()),
                valueOrDefault(command.importance(), existing.importance()),
                valueOrDefault(command.urgency(), existing.urgency()),
                valueOrDefault(command.type(), existing.type()),
                valueOrDefault(command.completed(), existing.completed())
        );
        return toDto(taskRepositoryPort.save(updated));
    }

    public void deleteTask(Long taskId) {
        taskRepositoryPort.findById(taskId).orElseThrow(() -> new TaskNotFoundException(taskId));
        taskRepositoryPort.deleteById(taskId);
    }

    private TaskDTO toDto(Task task) {
        return new TaskDTO(
                task.id(),
                task.title(),
                task.description(),
                task.deadline(),
                task.estimatedTime().toMinutes(),
                task.importance(),
                task.urgency(),
                task.type(),
                task.completed(),
                task.priorityScore()
        );
    }

    private Duration toDuration(Integer estimatedMinutes) {
        if (estimatedMinutes == null || estimatedMinutes <= 0) {
            return Duration.ofMinutes(30);
        }
        return Duration.ofMinutes(estimatedMinutes);
    }

    private <T> T valueOrDefault(T value, T fallback) {
        return value != null ? value : fallback;
    }
}
