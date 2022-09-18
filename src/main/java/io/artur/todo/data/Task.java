package io.artur.todo.data;

import com.mongodb.lang.Nullable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;

@Document
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Task {

    @Id
    private String id;

    @Field("userId")
    @DocumentReference
    private User userId;

    private String title;

    private String description;

    private Instant createdTs;

    @Nullable
    private Instant modifiedTs;
}
