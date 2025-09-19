package se.lexicon.todo_app.config;

import jakarta.annotation.PostConstruct;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import se.lexicon.todo_app.entity.Person;
import se.lexicon.todo_app.entity.Role;
import se.lexicon.todo_app.entity.User;
import se.lexicon.todo_app.repository.PersonRepository;
import se.lexicon.todo_app.repository.UserRepository;

/**
 * DataInitializer - creates initial users & persons for development environment.
 */
@Component
public class DataInitializer {

    private final UserRepository userRepository;
    private final PersonRepository personRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository,
                           PersonRepository personRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.personRepository = personRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    public void init() {
        // Only create default admin if not present
        String adminUsername = "admin";
        if (userRepository.existsById(adminUsername)) {
            return;
        }

        // create user
        User adminUser = new User();
        adminUser.setUsername(adminUsername);
        adminUser.setPassword(passwordEncoder.encode("password"));
        adminUser.addRole(Role.ADMIN);
        adminUser.addRole(Role.USER);
        adminUser.setExpired(false);
        userRepository.save(adminUser);

        // create person profile
        Person adminPerson = new Person();
        adminPerson.setFirstName("Admin");
        adminPerson.setLastName("User");
        adminPerson.setEmail("admin@test.se");
        adminPerson.setUser(adminUser);
        personRepository.save(adminPerson);

        // Example additional user
        String u1 = "mehrdad";
        if (!userRepository.existsById(u1)) {
            User u = new User();
            u.setUsername(u1);
            u.setPassword(passwordEncoder.encode("password"));
            u.addRole(Role.USER);
            u.setExpired(false);
            userRepository.save(u);

            Person p = new Person();
            p.setFirstName("Mehrdad");
            p.setLastName("Javan");
            p.setEmail("mehrdad@test.se");
            p.setUser(u);
            personRepository.save(p);
        }

        String u2 = "martin";
        if (!userRepository.existsById(u2)) {
            User u = new User();
            u.setUsername(u2);
            u.setPassword(passwordEncoder.encode("password"));
            u.addRole(Role.USER);
            u.setExpired(false);
            userRepository.save(u);

            Person p = new Person();
            p.setFirstName("Martin");
            p.setLastName("Josefsson");
            p.setEmail("martin@test.se");
            p.setUser(u);
            personRepository.save(p);
        }
    }
}
