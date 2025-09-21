package se.lexicon.todo_app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.lexicon.todo_app.dto.PersonDto;
import se.lexicon.todo_app.dto.PersonRegistrationDto;
import se.lexicon.todo_app.entity.Person;
import se.lexicon.todo_app.repository.PersonRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class PersonServiceImpl implements PersonService {

    private final PersonRepository personRepository;

    @Autowired
    public PersonServiceImpl(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Override
    public List<PersonDto> findAll() {
        return personRepository.findAll()
                .stream()
                .map(p -> new PersonDto(p.getId(), p.getFirstName(), p.getLastName(), p.getEmail()))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<PersonDto> findById(Long id) {
        return personRepository.findById(id)
                .map(p -> new PersonDto(p.getId(), p.getFirstName(), p.getLastName(), p.getEmail()));
    }

    @Override
    public Optional<PersonDto> findByEmail(String email) {
        return personRepository.findByEmail(email)
                .map(p -> new PersonDto(p.getId(), p.getFirstName(), p.getLastName(), p.getEmail()));
    }

    @Override
    public PersonDto create(PersonRegistrationDto dto) {
        if (personRepository.existsByEmail(dto.email())) {
            throw new IllegalArgumentException("Email already in use");
        }
        Person person = new Person(null, dto.firstName(), dto.lastName(), dto.email(), null);
        Person saved = personRepository.save(person);
        return new PersonDto(saved.getId(), saved.getFirstName(), saved.getLastName(), saved.getEmail());
    }

    @Override
    public PersonDto update(Long id, PersonDto dto) {
        Person person = personRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Person not found"));

        person.setFirstName(dto.firstName());
        person.setLastName(dto.lastName());
        person.setEmail(dto.email());

        Person updated = personRepository.save(person);
        return new PersonDto(updated.getId(), updated.getFirstName(), updated.getLastName(), updated.getEmail());
    }

    @Override
    public void delete(Long id) {
        if (!personRepository.existsById(id)) {
            throw new IllegalArgumentException("Person not found");
        }
        personRepository.deleteById(id);
    }
}
