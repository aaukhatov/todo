package io.artur.todo.api.v1;

import io.artur.todo.api.ErrorResponse;
import io.artur.todo.service.TaskNotFoundException;
import io.artur.todo.service.TaskServiceException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;

@ResponseBody
@ControllerAdvice
public class TaskExceptionHandler extends ResponseEntityExceptionHandler {

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(TaskServiceException.class)
    public ErrorResponse handleTaskExceptions(TaskServiceException ex) {
        return new ErrorResponse(List.of(new ErrorResponse.Error(ex.getMessage())));
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(TaskNotFoundException.class)
    public ErrorResponse handleTaskNotFound(TaskNotFoundException ex) {
        return new ErrorResponse(List.of(new ErrorResponse.Error(ex.getMessage())));
    }
}
