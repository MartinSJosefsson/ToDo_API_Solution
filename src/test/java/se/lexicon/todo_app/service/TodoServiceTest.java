package se.lexicon.todo_app.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.multipart.MultipartFile;
import se.lexicon.todo_app.entity.Attachment;
import se.lexicon.todo_app.entity.Todo;
import se.lexicon.todo_app.repository.AttachmentRepository;
import se.lexicon.todo_app.repository.TodoRepository;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TodoServiceTest {

    @Mock
    private TodoRepository todoRepository;

    @Mock
    private AttachmentRepository attachmentRepository;

    @InjectMocks
    private TodoServiceImpl todoService;

    private Todo todo;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        todo = new Todo();
        todo.setId(1L);
        todo.setTitle("Test Task");
        todo.setDescription("Description");
        todo.setCompleted(false);
    }

    @Test
    void testCreateTodo() {
        when(todoRepository.save(any(Todo.class))).thenReturn(todo);

        Todo created = todoService.create(todo);

        assertNotNull(created);
        assertEquals("Test Task", created.getTitle());
        verify(todoRepository, times(1)).save(todo);
    }

    @Test
    void testFindById() {
        when(todoRepository.findById(1L)).thenReturn(Optional.of(todo));

        Optional<Todo> found = todoService.findById(1L);

        assertTrue(found.isPresent());
        assertEquals("Test Task", found.get().getTitle());
    }

    @Test
    void testFindAll() {
        when(todoRepository.findAll()).thenReturn(Arrays.asList(todo));

        List<Todo> todos = todoService.findAll();

        assertEquals(1, todos.size());
    }

    @Test
    void testUpdateTodo() {
        when(todoRepository.findById(1L)).thenReturn(Optional.of(todo));
        when(todoRepository.save(any(Todo.class))).thenReturn(todo);

        Todo updated = new Todo();
        updated.setTitle("Updated");
        updated.setDescription("New Desc");
        updated.setCompleted(true);

        Todo result = todoService.update(1L, updated);

        assertEquals("Updated", result.getTitle());
        assertTrue(result.isCompleted());
    }

    @Test
    void testDeleteTodo() {
        doNothing().when(todoRepository).deleteById(1L);

        todoService.delete(1L);

        verify(todoRepository, times(1)).deleteById(1L);
    }

    @Test
    void testGetAttachments() {
        Attachment attachment = new Attachment();
        attachment.setId(1L);
        attachment.setFilename("test.txt");
        attachment.setTodo(todo);

        when(attachmentRepository.findByTodoId(1L)).thenReturn(Arrays.asList(attachment));

        List<Attachment> attachments = todoService.getAttachments(1L);

        assertEquals(1, attachments.size());
        assertEquals("test.txt", attachments.get(0).getFilename());
    }
}
