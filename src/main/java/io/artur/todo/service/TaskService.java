package io.artur.todo.service;

import io.artur.todo.api.data.TaskCreateRequest;
import io.artur.todo.api.data.TaskUpdateRequest;
import io.artur.todo.data.Task;
import io.artur.todo.data.User;
import io.artur.todo.repository.TaskRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class TaskService {

    private static final Logger log = LoggerFactory.getLogger(TaskService.class);

    private final TaskRepository repository;
    private final Clock systemUTC = Clock.systemUTC();

    @Autowired
    public TaskService(TaskRepository repository) {
        this.repository = repository;
    }

    public List<Task> getAllByUserId(String userId) {
        log.info("Getting TODOs by userId: {}", userId);
        return repository.findAll();
    }

    public Task create(TaskCreateRequest createTask) {
        return repository.save(new Task(
                UUID.randomUUID().toString(),
                new User(
                        UUID.randomUUID().toString(),
                        "email",
                        "Artur",
                        Instant.now(systemUTC),
                        null
                ),
                createTask.getTitle(),
                createTask.getDescription(),
                Instant.now(systemUTC),
                null
        ));
    }

    public Task update(String taskId, TaskUpdateRequest request) {
        Task task = repository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException(""));

        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());

        return repository.save(task);
    }

    public void delete(String taskId) {
        try {
            repository.deleteById(taskId);
        } catch (Exception e) {
            throw new TaskServiceException("Maybe task %s is already deleted".formatted(taskId), e);
        }
    }
}
