package se.lexicon.todo_app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import se.lexicon.todo_app.entity.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    // Find user by username
    Optional<User> findByUsername(String username);

    // Check if a username already exists
    boolean existsByUsername(String username);
}
