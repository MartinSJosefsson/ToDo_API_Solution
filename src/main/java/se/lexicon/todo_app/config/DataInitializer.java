package se.lexicon.todo_app.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import se.lexicon.todo_app.entity.Person;
import se.lexicon.todo_app.entity.Role;
import se.lexicon.todo_app.entity.User;
import se.lexicon.todo_app.repository.PersonRepository;
import se.lexicon.todo_app.repository.UserRepository;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initData(
            UserRepository userRepository,
            PersonRepository personRepository,
            PasswordEncoder passwordEncoder
    ) {
        return args -> {
            if (!userRepository.existsById("admin")) {
                User admin = new User("admin", passwordEncoder.encode("password"));
                admin.addRole(Role.ADMIN);
                admin.addRole(Role.USER);
                userRepository.save(admin);

                Person adminPerson = new Person();
                adminPerson.setFirstName("System");
                adminPerson.setLastName("Administrator");
                adminPerson.setEmail("admin@test.se");
                adminPerson.setUser(admin);
                personRepository.save(adminPerson);

                System.out.println("✅ Default admin user created: username=admin, password=password");
            } else {
                System.out.println("ℹ️ Admin user already exists.");
            }
        };
    }
}
