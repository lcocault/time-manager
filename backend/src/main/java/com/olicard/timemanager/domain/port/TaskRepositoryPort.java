package com.olicard.timemanager.domain.port;

import com.olicard.timemanager.domain.model.Task;
import java.util.List;
import java.util.Optional;

public interface TaskRepositoryPort {
    Task save(Task task);
    List<Task> findAll();
    Optional<Task> findById(Long id);
    void deleteById(Long id);
}
