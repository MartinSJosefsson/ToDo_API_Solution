package se.lexicon.todo_app.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import se.lexicon.todo_app.entity.Todo;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class TodoRepositoryTest {

    @Autowired
    private TodoRepository todoRepository;

    private Todo todo;

    @BeforeEach
    void setUp() {
        todo = new Todo();
        todo.setTitle("Shopping");
        todo.setDescription("Buy groceries");
        todo.setCompleted(false);
        todo.setDueDate(LocalDateTime.now().plusDays(1));

        todoRepository.save(todo);
    }

    @Test
    void testFindAll() {
        List<Todo> todos = todoRepository.findAll();
        assertFalse(todos.isEmpty());
        assertEquals("Shopping", todos.get(0).getTitle());
    }

    @Test
    void testSaveTodo() {
        Todo newTodo = new Todo();
        newTodo.setTitle("Study");
        newTodo.setDescription("Learn Spring Boot");
        newTodo.setCompleted(true);
        newTodo.setDueDate(LocalDateTime.now().plusDays(2));

        Todo saved = todoRepository.save(newTodo);

        assertNotNull(saved.getId());
        assertEquals("Study", saved.getTitle());
    }

    @Test
    void testDeleteTodo() {
        todoRepository.delete(todo);
        List<Todo> todos = todoRepository.findAll();
        assertTrue(todos.isEmpty());
    }
}
