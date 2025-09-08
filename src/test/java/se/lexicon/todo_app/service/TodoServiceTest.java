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
import java.time.LocalDateTime;
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
        todo.setDescription("Test Description");
        todo.setCompleted(false);
        todo.setDueDate(LocalDateTime.now().plusDays(1));
    }

    @Test
    void testGetAllTodos() {
        when(todoRepository.findAll()).thenReturn(Arrays.asList(todo));

        List<Todo> todos = todoService.getAllTodos();

        assertNotNull(todos);
        assertEquals(1, todos.size());
        verify(todoRepository, times(1)).findAll();
    }

    @Test
    void testCreateTodo() {
        when(todoRepository.save(todo)).thenReturn(todo);

        Todo saved = todoService.createTodo(todo);

        assertNotNull(saved);
        assertEquals("Test Task", saved.getTitle());
        verify(todoRepository, times(1)).save(todo);
    }

    @Test
    void testUpdateTodo() {
        when(todoRepository.findById(1L)).thenReturn(Optional.of(todo));
        when(todoRepository.save(any(Todo.class))).thenReturn(todo);

        Todo updated = todoService.updateTodo(1L, todo);

        assertNotNull(updated);
        verify(todoRepository, times(1)).findById(1L);
        verify(todoRepository, times(1)).save(todo);
    }

    @Test
    void testDeleteTodo() {
        when(todoRepository.findById(1L)).thenReturn(Optional.of(todo));
        doNothing().when(todoRepository).delete(todo);

        todoService.deleteTodo(1L);

        verify(todoRepository, times(1)).delete(todo);
    }

    @Test
    void testSaveAttachments() throws IOException {
        MultipartFile mockFile = mock(MultipartFile.class);
        when(mockFile.getOriginalFilename()).thenReturn("file.txt");

        when(todoRepository.findById(1L)).thenReturn(Optional.of(todo));

        todoService.saveAttachments(1L, List.of(mockFile));

        verify(attachmentRepository, times(1)).save(any(Attachment.class));
    }

    @Test
    void testGetAttachments() {
        Attachment attachment = new Attachment();
        attachment.setId(1L);
        attachment.setFilename("file.txt");

        when(todoRepository.findById(1L)).thenReturn(Optional.of(todo));
        when(attachmentRepository.findByTodoId(1L)).thenReturn(List.of(attachment));

        List<Attachment> attachments = todoService.getAttachments(1L);

        assertNotNull(attachments);
        assertEquals(1, attachments.size());
    }
}
