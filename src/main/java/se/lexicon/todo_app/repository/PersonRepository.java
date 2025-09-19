package se.lexicon.todo_app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import se.lexicon.todo_app.entity.Person;

import java.util.Optional;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {

    // Custom query method to fetch Person by related User’s username
    Optional<Person> findByUserUsername(String username);

    boolean existsByUserUsername(String username);
}
