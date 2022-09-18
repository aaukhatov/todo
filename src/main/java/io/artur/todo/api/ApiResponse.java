package io.artur.todo.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * There is a naive implementation of JSON API
 */
@Data
public class ApiResponse<T> {

    @JsonProperty("data")
    private T data;

    private ApiResponse(T data) {
        this.data = data;
    }

    public static <T> ApiResponse<T> result(T body) {
        return new ApiResponse<>(body);
    }
}
