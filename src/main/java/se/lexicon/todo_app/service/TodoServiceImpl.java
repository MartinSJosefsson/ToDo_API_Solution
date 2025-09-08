package se.lexicon.todo_app.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import se.lexicon.todo_app.entity.Attachment;
import se.lexicon.todo_app.entity.Todo;
import se.lexicon.todo_app.repository.AttachmentRepository;
import se.lexicon.todo_app.repository.TodoRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

@Service
public class TodoServiceImpl implements TodoService {

    private final TodoRepository todoRepository;
    private final AttachmentRepository attachmentRepository;
    private final String uploadDir = "uploads";

    public TodoServiceImpl(TodoRepository todoRepository,
                           AttachmentRepository attachmentRepository) {
        this.todoRepository = todoRepository;
        this.attachmentRepository = attachmentRepository;

        // Ensure upload folder exists
        try {
            Files.createDirectories(Path.of(uploadDir));
        } catch (IOException e) {
            throw new RuntimeException("Could not create upload folder", e);
        }
    }

    @Override
    public List<Todo> getAllTodos() {
        return todoRepository.findAll();
    }

    @Override
    public Todo createTodo(Todo todo) {
        return todoRepository.save(todo);
    }

    @Override
    public Todo updateTodo(Long id, Todo updatedTodo) {
        return todoRepository.findById(id)
                .map(todo -> {
                    todo.setTitle(updatedTodo.getTitle());
                    todo.setDescription(updatedTodo.getDescription());
                    todo.setCompleted(updatedTodo.isCompleted());
                    todo.setDueDate(updatedTodo.getDueDate());
                    return todoRepository.save(todo);
                })
                .orElseThrow(() -> new RuntimeException("Todo not found with id " + id));
    }

    @Override
    public void deleteTodo(Long id) {
        todoRepository.deleteById(id);
    }

    @Override
    public void saveAttachments(Long todoId, List<MultipartFile> files) throws IOException {
        Todo todo = todoRepository.findById(todoId)
                .orElseThrow(() -> new RuntimeException("Todo not found with id " + todoId));

        for (MultipartFile file : files) {
            String filePath = uploadDir + "/" + System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Files.write(Path.of(filePath), file.getBytes());

            Attachment attachment = new Attachment();
            attachment.setTodo(todo);
            attachment.setFilename(file.getOriginalFilename());
            attachment.setFilePath(filePath);

            attachmentRepository.save(attachment);
        }
    }

    @Override
    public List<Attachment> getAttachments(Long todoId) {
        return attachmentRepository.findByTodoId(todoId);
    }

    @Override
    public Attachment getAttachmentFile(Long todoId, Long attachmentId) {
        Optional<Attachment> attachment = attachmentRepository.findById(attachmentId);
        return attachment.orElseThrow(() -> new RuntimeException("Attachment not found"));
    }

    @Override
    public void deleteAttachment(Long todoId, Long attachmentId) {
        Attachment attachment = attachmentRepository.findById(attachmentId)
                .orElseThrow(() -> new RuntimeException("Attachment not found with id " + attachmentId));

        try {
            Files.deleteIfExists(Path.of(attachment.getFilePath()));
        } catch (IOException e) {
            throw new RuntimeException("Could not delete file " + attachment.getFilePath(), e);
        }

        attachmentRepository.delete(attachment);
    }
}
