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

    private Todo todo1;
    private Todo todo2;
    private Todo todo3;

    @BeforeEach
    void setUp() {
        LocalDateTime now = LocalDateTime.now();

        todo1 = new Todo();
        todo1.setTitle("Shopping");
        todo1.setDescription("Buy groceries");
        todo1.setCompleted(false);
        todo1.setDueDate(now.plusDays(1));

        todo2 = new Todo();
        todo2.setTitle("Study");
        todo2.setDescription("Learn Spring Boot");
        todo2.setCompleted(true);
        todo2.setDueDate(now.plusDays(2));

        todo3 = new Todo();
        todo3.setTitle("Reading");
        todo3.setDescription("Read chapter 5");
        todo3.setCompleted(false);
        todo3.setDueDate(now.minusDays(1));

        todoRepository.saveAll(List.of(todo1, todo2, todo3));
    }

    @Test
    void testFindAll() {
        List<Todo> todos = todoRepository.findAll();
        assertEquals(3, todos.size());
    }

    @Test
    void testFindByCompleted() {
        List<Todo> completedTodos = todoRepository.findByCompleted(true);
        assertEquals(1, completedTodos.size());
        assertEquals("Study", completedTodos.get(0).getTitle());
    }

    @Test
    void testFindByTitleContainingIgnoreCase() {
        List<Todo> results = todoRepository.findByTitleContainingIgnoreCase("shop");
        assertEquals(1, results.size());
        assertEquals("Shopping", results.get(0).getTitle());
    }

    @Test
    void testFindByDueDateBeforeAndNotCompleted() {
        LocalDateTime now = LocalDateTime.now();
        List<Todo> overdue = todoRepository.findByDueDateBeforeAndCompletedFalse(now);
        assertEquals(1, overdue.size());
        assertEquals("Reading", overdue.get(0).getTitle());
    }
}
