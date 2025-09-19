package se.lexicon.todo_app.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import se.lexicon.todo_app.entity.Person;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class PersonRepositoryTest {

    @Autowired
    private PersonRepository personRepository;

    @Test
    void testSaveAndFindPerson() {
        Person person = new Person();
        person.setFirstName("Mehrdad");
        person.setLastName("Javan");
        person.setEmail("mehrdad@test.se");

        person = personRepository.save(person);

        Optional<Person> found = personRepository.findById(person.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getFirstName()).isEqualTo("Mehrdad");
        assertThat(found.get().getLastName()).isEqualTo("Javan");
        assertThat(found.get().getEmail()).isEqualTo("mehrdad@test.se");
    }

    @Test
    void testExistsById() {
        Person person = new Person();
        person.setFirstName("Martin");
        person.setLastName("Josefsson");
        person.setEmail("martin@test.se");

        person = personRepository.save(person);

        boolean exists = personRepository.existsById(person.getId());

        assertThat(exists).isTrue();
    }
}
