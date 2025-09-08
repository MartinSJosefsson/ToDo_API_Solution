package se.lexicon.todo_app.service;

import org.springframework.web.multipart.MultipartFile;
import se.lexicon.todo_app.entity.Attachment;
import se.lexicon.todo_app.entity.Todo;

import java.io.IOException;
import java.util.List;

public interface TodoService {

    // --- Basic CRUD ---
    List<Todo> getAllTodos();

    Todo createTodo(Todo todo);

    Todo updateTodo(Long id, Todo updatedTodo);

    void deleteTodo(Long id);

    // --- Attachments ---
    void saveAttachments(Long todoId, List<MultipartFile> files) throws IOException;

    List<Attachment> getAttachments(Long todoId);

    Attachment getAttachmentFile(Long todoId, Long attachmentId);

    void deleteAttachment(Long todoId, Long attachmentId);
}
