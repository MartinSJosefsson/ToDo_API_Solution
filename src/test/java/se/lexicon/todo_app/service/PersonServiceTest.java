package se.lexicon.todo_app.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import se.lexicon.todo_app.dto.PersonDto;
import se.lexicon.todo_app.dto.PersonRegistrationDto;
import se.lexicon.todo_app.entity.Person;
import se.lexicon.todo_app.entity.User;
import se.lexicon.todo_app.repository.PersonRepository;
import se.lexicon.todo_app.repository.UserRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PersonServiceTest {

    @Mock
    private PersonRepository personRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private PersonServiceImpl personService;

    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User("testuser", "password");
    }

    @Test
    void testFindAll() {
        Person p = new Person("John", "Doe", "john.doe@example.com", user);
        when(personRepository.findAll()).thenReturn(List.of(p));

        List<PersonDto> result = personService.findAll();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).email()).isEqualTo("john.doe@example.com");
    }

    @Test
    void testFindById() {
        Person p = new Person("Jane", "Doe", "jane.doe@example.com", user);
        when(personRepository.findById(1L)).thenReturn(Optional.of(p));

        Optional<PersonDto> result = personService.findById(1L);
        assertThat(result).isPresent();
        assertThat(result.get().email()).isEqualTo("jane.doe@example.com");
    }

    @Test
    void testCreate() {
        PersonRegistrationDto dto = new PersonRegistrationDto("Alice", "Smith", "alice@example.com", "testuser");
        when(userRepository.findById("testuser")).thenReturn(Optional.of(user));
        when(personRepository.save(any(Person.class))).thenAnswer(invocation -> invocation.getArgument(0));

        PersonDto result = personService.create(dto);

        assertThat(result.firstName()).isEqualTo("Alice");
        assertThat(result.email()).isEqualTo("alice@example.com");
    }

    @Test
    void testDelete() {
        personService.delete(1L);
        verify(personRepository, times(1)).deleteById(1L);
    }
}
