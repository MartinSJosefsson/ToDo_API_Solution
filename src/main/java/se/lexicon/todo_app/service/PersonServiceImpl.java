package se.lexicon.todo_app.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.lexicon.todo_app.dto.PersonDto;
import se.lexicon.todo_app.dto.PersonRegistrationDto;
import se.lexicon.todo_app.entity.Person;
import se.lexicon.todo_app.entity.User;
import se.lexicon.todo_app.repository.PersonRepository;
import se.lexicon.todo_app.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class PersonServiceImpl implements PersonService {

    private final PersonRepository personRepository;
    private final UserRepository userRepository;

    public PersonServiceImpl(PersonRepository personRepository, UserRepository userRepository) {
        this.personRepository = personRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<PersonDto> findAll() {
        return personRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public PersonDto findById(Long id) {
        return personRepository.findById(id)
                .map(this::toDto)
                .orElseThrow(() -> new RuntimeException("Person not found with id: " + id));
    }

    @Override
    public PersonDto create(PersonRegistrationDto registrationDto) {
        Person person = new Person();
        person.setFirstName(registrationDto.firstName());
        person.setLastName(registrationDto.lastName());
        person.setEmail(registrationDto.email());

        // link to a user if needed (optional)
        if (!userRepository.existsById(registrationDto.email())) {
            User user = new User(registrationDto.email(), "defaultPassword");
            userRepository.save(user);
            person.setUser(user);
        }

        return toDto(personRepository.save(person));
    }

    @Override
    public PersonDto update(Long id, PersonDto dto) {
        Person person = personRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Person not found with id: " + id));

        person.setFirstName(dto.firstName());
        person.setLastName(dto.lastName());
        person.setEmail(dto.email());

        return toDto(personRepository.save(person));
    }

    @Override
    public void delete(Long id) {
        if (!personRepository.existsById(id)) {
            throw new RuntimeException("Person not found with id: " + id);
        }
        personRepository.deleteById(id);
    }

    private PersonDto toDto(Person person) {
        return new PersonDto(
                person.getId(),
                person.getFirstName(),
                person.getLastName(),
                person.getEmail()
        );
    }
}
