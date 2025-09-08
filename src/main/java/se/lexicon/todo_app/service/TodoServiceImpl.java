package se.lexicon.todo_app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import se.lexicon.todo_app.entity.Attachment;
import se.lexicon.todo_app.entity.Todo;
import se.lexicon.todo_app.repository.AttachmentRepository;
import se.lexicon.todo_app.repository.TodoRepository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@Service
public class TodoServiceImpl implements TodoService {

    private final TodoRepository todoRepository;
    private final AttachmentRepository attachmentRepository;
    private final String uploadDir = "uploads"; // local dir for file storage

    @Autowired
    public TodoServiceImpl(TodoRepository todoRepository, AttachmentRepository attachmentRepository) {
        this.todoRepository = todoRepository;
        this.attachmentRepository = attachmentRepository;

        // ensure upload folder exists
        File dir = new File(uploadDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    // --- CRUD ---

    @Override
    public List<Todo> findAll() {
        return todoRepository.findAll();
    }

    @Override
    public Optional<Todo> findById(Long id) {
        return todoRepository.findById(id);
    }

    @Override
    public Todo create(Todo todo) {
        return todoRepository.save(todo);
    }

    @Override
    public Todo update(Long id, Todo updatedTodo) {
        return todoRepository.findById(id).map(existing -> {
            existing.setTitle(updatedTodo.getTitle());
            existing.setDescription(updatedTodo.getDescription());
            existing.setCompleted(updatedTodo.isCompleted());
            existing.setDueDate(updatedTodo.getDueDate());
            return todoRepository.save(existing);
        }).orElseThrow(() -> new RuntimeException("Todo not found with id " + id));
    }

    @Override
    public void delete(Long id) {
        todoRepository.deleteById(id);
    }

    // --- Attachments ---

    @Override
    public List<Attachment> saveAttachments(Long todoId, List<MultipartFile> files) throws IOException {
        Todo todo = todoRepository.findById(todoId)
                .orElseThrow(() -> new RuntimeException("Todo not found with id " + todoId));

        for (MultipartFile file : files) {
            String filePath = uploadDir + File.separator + file.getOriginalFilename();
            Files.write(Paths.get(filePath), file.getBytes());

            Attachment attachment = new Attachment();
            attachment.setFilename(file.getOriginalFilename());
            attachment.setFilePath(filePath);
            attachment.setTodo(todo);

            attachmentRepository.save(attachment);
        }

        return attachmentRepository.findByTodoId(todoId);
    }

    @Override
    public List<Attachment> getAttachments(Long todoId) {
        return attachmentRepository.findByTodoId(todoId);
    }

    @Override
    public Attachment getAttachmentFile(Long todoId, Long attachmentId) {
        return attachmentRepository.findById(attachmentId)
                .filter(a -> a.getTodo().getId().equals(todoId))
                .orElseThrow(() -> new RuntimeException("Attachment not found"));
    }

    @Override
    public void deleteAttachment(Long todoId, Long attachmentId) {
        Attachment attachment = getAttachmentFile(todoId, attachmentId);

        File file = new File(attachment.getFilePath());
        if (file.exists()) {
            file.delete();
        }

        attachmentRepository.delete(attachment);
    }
}
