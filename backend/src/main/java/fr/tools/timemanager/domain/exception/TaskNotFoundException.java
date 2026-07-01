package fr.tools.timemanager.domain.exception;

public class TaskNotFoundException extends RuntimeException {
    public TaskNotFoundException(Long taskId) {
        super("Task " + taskId + " was not found");
    }
}
