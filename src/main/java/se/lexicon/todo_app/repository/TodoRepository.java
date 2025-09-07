package se.lexicon.todo_app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import se.lexicon.todo_app.entity.Todo;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TodoRepository extends JpaRepository<Todo, Long> {

    // ✅ used in tests
    List<Todo> findByTitleContainingIgnoreCase(String title);

    // ✅ person lookups
    List<Todo> findByPerson_Id(Long personId);
    List<Todo> findByPersonId(Long personId);

    // ✅ status lookups
    List<Todo> findByCompleted(boolean completed);

    // ✅ due date filters
    List<Todo> findByDueDateBetween(LocalDateTime start, LocalDateTime end);
    List<Todo> findByDueDateBeforeAndCompletedFalse(LocalDateTime dueDate);
    List<Todo> findByCompletedFalseAndDueDateBefore(LocalDateTime dueDate);
    List<Todo> findByDueDateIsNull();

    // ✅ person filters
    List<Todo> findByPersonIsNull();
    List<Todo> findByPersonIdAndCompletedTrue(Long personId);

    // ✅ count
    long countByPersonId(Long personId);
}
