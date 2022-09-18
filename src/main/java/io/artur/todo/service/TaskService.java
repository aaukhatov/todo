package io.artur.todo.service;

import io.artur.todo.api.data.TaskCreateRequest;
import io.artur.todo.api.data.TaskUpdateRequest;
import io.artur.todo.data.Task;
import io.artur.todo.data.User;
import io.artur.todo.repository.TaskRepository;
import io.artur.todo.repository.UserRepository;
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
    private final UserRepository userRepository;
    private final Clock systemUTC = Clock.systemUTC();

    @Autowired
    public TaskService(TaskRepository repository, UserRepository userRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
    }

    public List<Task> getAllByUserId(String userId) {
        log.info("Getting TODOs by userId: {}", userId);
        return repository.findByUserId(userId);
    }

    public Task create(TaskCreateRequest createTask, String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new TaskServiceException("Unexpected error, please try later"));
        log.info("Creating a new task...");
        return repository.save(new Task(
                UUID.randomUUID().toString(),
                user,
                createTask.getTitle(),
                createTask.getDescription(),
                Instant.now(systemUTC),
                null
        ));
    }

    public Task update(String taskId, String userId, TaskUpdateRequest request) {
        Task task = repository.findByIdAndUserId(taskId, userId)
                .orElseThrow(() -> new TaskNotFoundException("Cannot find the given task"));

        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setModifiedTs(Instant.now(systemUTC));
        log.info("Editing a task {}", taskId);
        return repository.save(task);
    }

    public void delete(String taskId, String userId) {
        repository.deleteByIdAndUserId(taskId, userId);
    }
}
