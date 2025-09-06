package se.lexicon.todo_app.service;

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
}
