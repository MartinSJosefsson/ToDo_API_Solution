package se.lexicon.todo_app.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import se.lexicon.todo_app.entity.Role;
import se.lexicon.todo_app.entity.User;
import se.lexicon.todo_app.repository.UserRepository;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initData(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            // Ensure admin user exists
            if (!userRepository.existsById("admin")) {
                User admin = new User("admin", passwordEncoder.encode("password"));
                admin.addRole(Role.ADMIN);
                admin.addRole(Role.USER);
                userRepository.save(admin);
                System.out.println("✅ Default admin user created: username=admin, password=password");
            } else {
                System.out.println("ℹ️ Admin user already exists.");
            }

            // Print all users for debugging
            System.out.println("📋 Current users in DB:");
            userRepository.findAll().forEach(user -> {
                System.out.println(" - Username: " + user.getUsername() + " | Roles: " + user.getRoles());
            });
        };
    }
}
