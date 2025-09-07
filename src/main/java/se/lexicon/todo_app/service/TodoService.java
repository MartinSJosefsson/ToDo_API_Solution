package se.lexicon.todo_app.service;

import org.springframework.web.multipart.MultipartFile;
import se.lexicon.todo_app.dto.TodoDto;

import java.util.List;

public interface TodoService {

    TodoDto create(TodoDto dto);

    TodoDto findById(Long id);

    List<TodoDto> findAll();

    TodoDto update(Long id, TodoDto dto);

    void delete(Long id);

    List<TodoDto> findByPersonId(Long personId);

    List<TodoDto> findByCompleted(boolean completed);

    // ✅ new methods for attachments
    void saveAttachments(Long todoId, List<MultipartFile> files);

    List<String> getAttachments(Long todoId);

    byte[] getAttachmentFile(Long todoId, Long attachmentId);
}
