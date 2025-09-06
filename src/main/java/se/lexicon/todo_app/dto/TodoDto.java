package se.lexicon.todo_app.dto;

import lombok.Builder;
import java.time.LocalDateTime;

@Builder
public record TodoDto(
        Long id,
        String title,
        String description,
        boolean completed,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        LocalDateTime dueDate,
        Long personId
) {}
