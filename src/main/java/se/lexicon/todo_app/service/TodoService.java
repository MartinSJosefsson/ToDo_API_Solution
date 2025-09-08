package se.lexicon.todo_app.service;

import org.springframework.web.multipart.MultipartFile;
import se.lexicon.todo_app.entity.Attachment;
import se.lexicon.todo_app.entity.Todo;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface TodoService {

    // --- CRUD for Todos ---
    List<Todo> findAll();

    Optional<Todo> findById(Long id);

    Todo create(Todo todo);

    Todo update(Long id, Todo updatedTodo);

    void delete(Long id);

    // --- Attachment handling ---
    List<Attachment> saveAttachments(Long todoId, List<MultipartFile> files) throws IOException;

    List<Attachment> getAttachments(Long todoId);

    Attachment getAttachmentFile(Long todoId, Long attachmentId);

    void deleteAttachment(Long todoId, Long attachmentId);
}
