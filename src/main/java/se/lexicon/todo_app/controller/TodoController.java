package se.lexicon.todo_app.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import se.lexicon.todo_app.entity.Attachment;
import se.lexicon.todo_app.entity.Todo;
import se.lexicon.todo_app.service.TodoService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/todos")
public class TodoController {

    private final TodoService todoService;

    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    @GetMapping
    public List<Todo> getAllTodos() {
        return todoService.getAllTodos();
    }

    @PostMapping
    public Todo createTodo(@RequestBody Todo todo) {
        return todoService.createTodo(todo);
    }

    @PutMapping("/{id}")
    public Todo updateTodo(@PathVariable Long id, @RequestBody Todo todo) {
        return todoService.updateTodo(id, todo);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTodo(@PathVariable Long id) {
        todoService.deleteTodo(id);
        return ResponseEntity.ok(Map.of("message", "Todo deleted successfully"));
    }

    // Upload attachments
    @PostMapping("/{id}/attachments")
    public ResponseEntity<?> uploadAttachments(@PathVariable Long id,
                                               @RequestParam("files") List<MultipartFile> files) throws IOException {
        todoService.saveAttachments(id, files);
        return ResponseEntity.ok(Map.of("message", "Files uploaded successfully"));
    }

    // Get all attachments for a Todo
    @GetMapping("/{id}/attachments")
    public List<Attachment> getAttachments(@PathVariable Long id) {
        return todoService.getAttachments(id);
    }

    // Download an attachment
    @GetMapping("/{todoId}/attachments/{attachmentId}")
    public ResponseEntity<byte[]> downloadAttachment(@PathVariable Long todoId,
                                                     @PathVariable Long attachmentId) throws IOException {
        Attachment attachment = todoService.getAttachmentFile(todoId, attachmentId);
        Path filePath = Path.of(attachment.getFilePath());

        byte[] fileContent = Files.readAllBytes(filePath);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + attachment.getFilename() + "\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(fileContent);
    }

    // Delete an attachment
    @DeleteMapping("/{todoId}/attachments/{attachmentId}")
    public ResponseEntity<?> deleteAttachment(@PathVariable Long todoId, @PathVariable Long attachmentId) {
        todoService.deleteAttachment(todoId, attachmentId);
        return ResponseEntity.ok(Map.of("message", "Attachment deleted successfully"));
    }
}
