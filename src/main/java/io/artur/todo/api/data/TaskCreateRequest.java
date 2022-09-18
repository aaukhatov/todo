package io.artur.todo.api.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskCreateRequest {

    @NotEmpty(message = "Title must be specified")
    @JsonProperty("title")
    private String title;

    @NotEmpty(message = "Description must be specified")
    @JsonProperty("description")
    private String description;
}
