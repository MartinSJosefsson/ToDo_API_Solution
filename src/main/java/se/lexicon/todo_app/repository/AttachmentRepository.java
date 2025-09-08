package se.lexicon.todo_app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.lexicon.todo_app.entity.Attachment;

import java.util.List;

public interface AttachmentRepository extends JpaRepository<Attachment, Long> {
    List<Attachment> findByTodoId(Long todoId);
}
