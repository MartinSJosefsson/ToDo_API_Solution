package se.lexicon.todo_app.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "attachments")
public class Attachment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String filename;

    private String filePath;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "todo_id")
    private Todo todo;

    // --- Constructors ---
    public Attachment() {}

    public Attachment(String filename, String filePath, Todo todo) {
        this.filename = filename;
        this.filePath = filePath;
        this.todo = todo;
    }

    // --- Getters & Setters ---
    public Long getId() {
        return id;
    }

    // ✅ Added setter so tests can use setId()
    public void setId(Long id) {
        this.id = id;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Todo getTodo() {
        return todo;
    }

    public void setTodo(Todo todo) {
        this.todo = todo;
    }
}
