package se.lexicon.todo_app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import se.lexicon.todo_app.entity.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    // Find user by username
    Optional<User> findByUsername(String username);

    // Check if a user exists by username
    boolean existsByUsername(String username);

    // Update password by username
    @Modifying
    @Query("UPDATE User u SET u.password = :password WHERE u.username = :username")
    void updatePasswordByUsername(@Param("username") String username, @Param("password") String password);

    // Update expired flag by username
    @Modifying
    @Query("UPDATE User u SET u.expired = :expired WHERE u.username = :username")
    void updateExpiredByUsername(@Param("username") String username, @Param("expired") boolean expired);
}
