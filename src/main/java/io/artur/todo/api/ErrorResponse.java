package io.artur.todo.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ErrorResponse {

    @JsonProperty("errors")
    private List<Error> errors;

    @Data
    @AllArgsConstructor
    public static class Error {

        @JsonProperty("detail")
        private String detail;
    }
}
