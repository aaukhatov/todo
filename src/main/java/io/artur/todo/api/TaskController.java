package io.artur.todo.api;

import io.artur.todo.api.data.TaskCreateRequest;
import io.artur.todo.api.data.TaskResponse;
import io.artur.todo.api.data.TaskUpdateRequest;
import io.artur.todo.data.Task;
import io.artur.todo.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1")
public class TaskController {

    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping(path = "/tasks")
    @ResponseStatus(HttpStatus.OK)
    public List<TaskResponse> getAll() {

        return taskService.getAllByUserId("")
                .stream()
                .map(t -> new TaskResponse(t.getId(), t.getTitle(), t.getDescription()))
                .collect(Collectors.toList());
    }

    @PostMapping("/tasks")
    @ResponseStatus(HttpStatus.CREATED)
    public TaskResponse create(@RequestBody @Valid TaskCreateRequest request) {

        Task task = taskService.create(request);
        return new TaskResponse(task.getId(), task.getTitle(), task.getDescription());
    }

    @PutMapping("/tasks/{id}")
    @ResponseStatus(HttpStatus.OK)
    public TaskResponse update(@PathVariable("id") String taskId,
                               @RequestBody @Valid TaskUpdateRequest request) {

        Task task = taskService.update(taskId, request);
        return new TaskResponse(task.getId(), task.getTitle(), task.getDescription());
    }

    @DeleteMapping("/tasks/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") String taskId) {

        taskService.delete(taskId);
    }
}
