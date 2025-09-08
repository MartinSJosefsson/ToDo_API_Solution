package se.lexicon.todo_app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import se.lexicon.todo_app.entity.Attachment;

import java.util.List;
import java.util.Optional;

@Repository
public interface AttachmentRepository extends JpaRepository<Attachment, Long> {

    // All attachments belonging to a specific todo
    List<Attachment> findByTodoId(Long todoId);

    // A specific attachment by ID, scoped to a todo
    Optional<Attachment> findByIdAndTodoId(Long id, Long todoId);
}
