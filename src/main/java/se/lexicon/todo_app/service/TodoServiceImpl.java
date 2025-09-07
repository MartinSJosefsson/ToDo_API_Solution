package se.lexicon.todo_app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import se.lexicon.todo_app.dto.TodoDto;
import se.lexicon.todo_app.entity.Attachment;
import se.lexicon.todo_app.entity.Person;
import se.lexicon.todo_app.entity.Todo;
import se.lexicon.todo_app.repository.AttachmentRepository;
import se.lexicon.todo_app.repository.PersonRepository;
import se.lexicon.todo_app.repository.TodoRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TodoServiceImpl implements TodoService {

    private final TodoRepository todoRepository;
    private final PersonRepository personRepository;
    private final AttachmentRepository attachmentRepository;

    private final String uploadDir = "uploads"; // directory for file storage

    @Autowired
    public TodoServiceImpl(TodoRepository todoRepository,
                           PersonRepository personRepository,
                           AttachmentRepository attachmentRepository) {
        this.todoRepository = todoRepository;
        this.personRepository = personRepository;
        this.attachmentRepository = attachmentRepository;
    }

    @Override
    public TodoDto create(TodoDto dto) {
        Todo todo = new Todo();
        todo.setTitle(dto.title());
        todo.setDescription(dto.description());
        todo.setCompleted(dto.completed());
        todo.setDueDate(dto.dueDate());

        if (dto.personId() != null) {
            Person person = personRepository.findById(dto.personId())
                    .orElseThrow(() -> new RuntimeException("Person not found with id " + dto.personId()));
            todo.setPerson(person);
        }

        Todo saved = todoRepository.save(todo);
        return toDto(saved);
    }

    @Override
    public TodoDto update(Long id, TodoDto dto) {
        Todo todo = todoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Todo not found with id " + id));

        todo.setTitle(dto.title());
        todo.setDescription(dto.description());
        todo.setCompleted(dto.completed());
        todo.setDueDate(dto.dueDate());

        if (dto.personId() != null) {
            Person person = personRepository.findById(dto.personId())
                    .orElseThrow(() -> new RuntimeException("Person not found with id " + dto.personId()));
            todo.setPerson(person);
        } else {
            todo.setPerson(null);
        }

        Todo updated = todoRepository.save(todo);
        return toDto(updated);
    }

    @Override
    public TodoDto findById(Long id) {
        Todo todo = todoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Todo not found with id " + id));
        return toDto(todo);
    }

    @Override
    public List<TodoDto> findAll() {
        return todoRepository.findAll()
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<TodoDto> findByCompleted(boolean completed) {
        return todoRepository.findByCompleted(completed)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<TodoDto> findByPersonId(Long personId) {
        return todoRepository.findByPerson_Id(personId)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Long id) {
        todoRepository.deleteById(id);
    }

    @Override
    public void saveAttachments(Long todoId, List<MultipartFile> files) {
        Todo todo = todoRepository.findById(todoId)
                .orElseThrow(() -> new RuntimeException("Todo not found with id " + todoId));

        try {
            Files.createDirectories(Paths.get(uploadDir));

            for (MultipartFile file : files) {
                Path filePath = Paths.get(uploadDir, file.getOriginalFilename());
                Files.write(filePath, file.getBytes());

                Attachment attachment = new Attachment();
                attachment.setFilename(file.getOriginalFilename());
                attachment.setFilePath(filePath.toString());
                attachment.setTodo(todo);

                attachmentRepository.save(attachment);
            }

            todo.setNumberOfAttachments(todo.getAttachments().size());
            todoRepository.save(todo);

        } catch (IOException e) {
            throw new RuntimeException("Failed to save attachments", e);
        }
    }

    @Override
    public List<String> getAttachments(Long todoId) {
        return attachmentRepository.findByTodoId(todoId)
                .stream()
                .map(Attachment::getFilename)
                .collect(Collectors.toList());
    }

    @Override
    public byte[] getAttachmentFile(Long todoId, Long attachmentId) {
        Attachment attachment = attachmentRepository.findById(attachmentId)
                .orElseThrow(() -> new RuntimeException("Attachment not found with id " + attachmentId));

        try {
            return Files.readAllBytes(Paths.get(attachment.getFilePath()));
        } catch (IOException e) {
            throw new RuntimeException("Failed to read attachment file", e);
        }
    }

    // --- helper mapping method ---
    private TodoDto toDto(Todo todo) {
        return new TodoDto(
                todo.getId(),
                todo.getTitle(),
                todo.getDescription(),
                todo.isCompleted(),
                todo.getDueDate(),
                todo.getCreatedAt(),
                todo.getUpdatedAt(),
                todo.getPerson() != null ? todo.getPerson().getId() : null
        );
    }
}
