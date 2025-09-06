package se.lexicon.todo_app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.lexicon.todo_app.dto.TodoDto;
import se.lexicon.todo_app.entity.Person;
import se.lexicon.todo_app.entity.Todo;
import se.lexicon.todo_app.repository.PersonRepository;
import se.lexicon.todo_app.repository.TodoRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TodoServiceImpl implements TodoService {

    private final TodoRepository todoRepository;
    private final PersonRepository personRepository;

    @Autowired
    public TodoServiceImpl(TodoRepository todoRepository, PersonRepository personRepository) {
        this.todoRepository = todoRepository;
        this.personRepository = personRepository;
    }

    private TodoDto mapToDto(Todo todo) {
        return TodoDto.builder()
                .id(todo.getId())
                .title(todo.getTitle())
                .description(todo.getDescription())
                .completed(todo.isCompleted())
                .createdAt(todo.getCreatedAt())
                .updatedAt(todo.getUpdatedAt())
                .dueDate(todo.getDueDate())
                .personId(todo.getPerson() != null ? todo.getPerson().getId() : null)
                .build();
    }

    private Todo mapToEntity(TodoDto dto) {
        Todo todo = new Todo();
        todo.setId(dto.id());
        todo.setTitle(dto.title());
        todo.setDescription(dto.description());
        todo.setCompleted(dto.completed());
        todo.setCreatedAt(dto.createdAt() != null ? dto.createdAt() : LocalDateTime.now());
        todo.setUpdatedAt(LocalDateTime.now());
        todo.setDueDate(dto.dueDate());

        if (dto.personId() != null) {
            Person person = personRepository.findById(dto.personId())
                    .orElseThrow(() -> new RuntimeException("Person not found with id " + dto.personId()));
            todo.setPerson(person);
        } else {
            todo.setPerson(null);
        }

        return todo;
    }

    @Override
    public TodoDto create(TodoDto dto) {
        Todo saved = todoRepository.save(mapToEntity(dto));
        return mapToDto(saved);
    }

    @Override
    public TodoDto findById(Long id) {
        return todoRepository.findById(id)
                .map(this::mapToDto)
                .orElseThrow(() -> new RuntimeException("Todo not found with id " + id));
    }

    @Override
    public List<TodoDto> findAll() {
        return todoRepository.findAll()
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public TodoDto update(Long id, TodoDto dto) {
        return todoRepository.findById(id).map(existing -> {
            existing.setTitle(dto.title());
            existing.setDescription(dto.description());
            existing.setCompleted(dto.completed());
            existing.setDueDate(dto.dueDate());
            existing.setUpdatedAt(LocalDateTime.now());

            if (dto.personId() != null) {
                Person person = personRepository.findById(dto.personId())
                        .orElseThrow(() -> new RuntimeException("Person not found with id " + dto.personId()));
                existing.setPerson(person);
            } else {
                existing.setPerson(null);
            }

            return mapToDto(todoRepository.save(existing));
        }).orElseThrow(() -> new RuntimeException("Todo not found with id " + id));
    }

    @Override
    public void delete(Long id) {
        todoRepository.deleteById(id);
    }

    @Override
    public List<TodoDto> findByPersonId(Long personId) {
        return todoRepository.findByPersonId(personId)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<TodoDto> findByCompleted(boolean completed) {
        return todoRepository.findByCompleted(completed)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }
}
