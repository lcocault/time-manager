package com.olicard.timemanager.infrastructure.persistence;

import com.olicard.timemanager.domain.model.Task;
import com.olicard.timemanager.domain.port.TaskRepositoryPort;
import java.time.Duration;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class TaskPersistenceAdapter implements TaskRepositoryPort {
    private final TaskJpaRepository taskJpaRepository;

    public TaskPersistenceAdapter(TaskJpaRepository taskJpaRepository) {
        this.taskJpaRepository = taskJpaRepository;
    }

    @Override
    public Task save(Task task) {
        return toDomain(taskJpaRepository.save(toEntity(task)));
    }

    @Override
    public List<Task> findAll() {
        return taskJpaRepository.findAll().stream().map(this::toDomain).toList();
    }

    @Override
    public Optional<Task> findById(Long id) {
        return taskJpaRepository.findById(id).map(this::toDomain);
    }

    @Override
    public void deleteById(Long id) {
        taskJpaRepository.deleteById(id);
    }

    private TaskEntity toEntity(Task task) {
        TaskEntity entity = new TaskEntity();
        entity.setId(task.id());
        entity.setTitle(task.title());
        entity.setDescription(task.description());
        entity.setDeadline(task.deadline());
        entity.setEstimatedMinutes((int) task.estimatedTime().toMinutes());
        entity.setImportance(task.importance());
        entity.setUrgency(task.urgency());
        entity.setType(task.type());
        entity.setCompleted(task.completed());
        return entity;
    }

    private Task toDomain(TaskEntity entity) {
        return new Task(
                entity.getId(),
                entity.getTitle(),
                entity.getDescription(),
                entity.getDeadline(),
                Duration.ofMinutes(entity.getEstimatedMinutes()),
                entity.getImportance(),
                entity.getUrgency(),
                entity.getType(),
                entity.isCompleted()
        );
    }
}
