package se.lexicon.todo_app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import se.lexicon.todo_app.entity.Person;

import java.util.Optional;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {

    // Find a person by email
    Optional<Person> findByEmail(String email);

    // Check if a person exists by email
    boolean existsByEmail(String email);
}
