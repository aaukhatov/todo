package io.artur.todo.service;

public class TaskNotFoundException extends TaskServiceException {
    public TaskNotFoundException(String message) {
        super(message);
    }

    public TaskNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
