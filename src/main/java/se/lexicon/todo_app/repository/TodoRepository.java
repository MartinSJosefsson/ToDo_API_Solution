package se.lexicon.todo_app.repository;

import se.lexicon.todo_app.entity.Todo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TodoRepository extends JpaRepository<Todo, Long> {

    List<Todo> findByCompleted(boolean completed);

    // ✅ tests may use both
    List<Todo> findByPerson_Id(Long personId);
    List<Todo> findByPersonId(Long personId);

    List<Todo> findByTitleContainingIgnoreCase(String title);

    List<Todo> findByDueDateBetween(LocalDateTime start, LocalDateTime end);

    List<Todo> findByDueDateBeforeAndCompletedFalse(LocalDateTime dueDate);

    List<Todo> findByDueDateIsNull();

    List<Todo> findByPersonIsNull();

    List<Todo> findByCompletedFalseAndDueDateBefore(LocalDateTime dueDate);

    List<Todo> findByPersonIdAndCompletedTrue(Long personId);

    long countByPersonId(Long personId);
}
