package se.lexicon.todo_app.service;

import se.lexicon.todo_app.dto.PersonDto;
import se.lexicon.todo_app.dto.PersonRegistrationDto;

import java.util.List;

public interface PersonService {
    List<PersonDto> findAll();
    PersonDto findById(Long id);
    PersonDto create(PersonRegistrationDto dto);
    PersonDto update(Long id, PersonDto dto);
    void delete(Long id);
}
