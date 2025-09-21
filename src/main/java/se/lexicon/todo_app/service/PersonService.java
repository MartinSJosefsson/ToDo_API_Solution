package se.lexicon.todo_app.service;

import se.lexicon.todo_app.dto.PersonDto;
import se.lexicon.todo_app.dto.PersonRegistrationDto;

import java.util.List;
import java.util.Optional;

public interface PersonService {
    List<PersonDto> findAll();
    Optional<PersonDto> findById(Long id);
    Optional<PersonDto> findByEmail(String email);
    PersonDto create(PersonRegistrationDto dto);
    PersonDto update(Long id, PersonDto dto);
    void delete(Long id);
}
