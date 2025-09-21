package se.lexicon.todo_app.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import se.lexicon.todo_app.entity.Person;
import se.lexicon.todo_app.entity.User;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class PersonRepositoryTest {

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void testSaveAndFindByEmail() {
        User user = new User("testuser", "password");
        userRepository.save(user);

        Person person = new Person("John", "Doe", "john.doe@example.com", user);
        personRepository.save(person);

        Optional<Person> found = personRepository.findByEmail("john.doe@example.com");
        assertThat(found).isPresent();
        assertThat(found.get().getFirstName()).isEqualTo("John");
    }

    @Test
    void testExistsByEmail() {
        User user = new User("anotheruser", "password");
        userRepository.save(user);

        Person person = new Person("Jane", "Doe", "jane.doe@example.com", user);
        personRepository.save(person);

        assertThat(personRepository.existsByEmail("jane.doe@example.com")).isTrue();
    }
}
