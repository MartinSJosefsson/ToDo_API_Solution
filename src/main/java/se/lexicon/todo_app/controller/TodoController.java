package se.lexicon.todo_app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import se.lexicon.todo_app.dto.TodoDto;
import se.lexicon.todo_app.service.TodoService;

import java.util.List;

@RestController
@RequestMapping("/api/todo")
public class TodoController {

    private final TodoService todoService;

    @Autowired
    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    // --- CRUD endpoints (as before) ---

    // ✅ upload attachments
    @PostMapping("/{id}/attachments")
    public ResponseEntity<String> uploadAttachments(@PathVariable Long id,
                                                    @RequestParam("files") List<MultipartFile> files) {
        todoService.saveAttachments(id, files);
        return ResponseEntity.ok("Files uploaded successfully");
    }

    // ✅ list attachment names
    @GetMapping("/{id}/attachments")
    public ResponseEntity<List<String>> getAttachments(@PathVariable Long id) {
        return ResponseEntity.ok(todoService.getAttachments(id));
    }

    // ✅ download a single attachment
    @GetMapping("/{id}/attachments/{attachmentId}")
    public ResponseEntity<byte[]> downloadAttachment(@PathVariable Long id,
                                                     @PathVariable Long attachmentId) {
        byte[] fileData = todoService.getAttachmentFile(id, attachmentId);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=file")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(fileData);
    }
}
