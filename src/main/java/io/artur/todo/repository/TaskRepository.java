package io.artur.todo.repository;

import io.artur.todo.data.Task;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends MongoRepository<Task, String> {
    List<Task> findByUserId(String userId);

    Optional<Task> findByIdAndUserId(String taskId, String userId);

    void deleteByIdAndUserId(String taskId, String userId);
}
