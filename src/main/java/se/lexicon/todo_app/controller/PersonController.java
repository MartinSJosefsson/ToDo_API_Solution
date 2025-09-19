package se.lexicon.todo_app.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.lexicon.todo_app.dto.PersonDto;
import se.lexicon.todo_app.dto.PersonRegistrationDto;
import se.lexicon.todo_app.service.PersonService;

import java.util.List;

@RestController
@RequestMapping("/api/persons")
public class PersonController {

    private final PersonService personService;

    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping
    public ResponseEntity<List<PersonDto>> getAll() {
        return ResponseEntity.ok(personService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PersonDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(personService.findById(id));
    }

    @PostMapping
    public ResponseEntity<PersonDto> create(@RequestBody PersonRegistrationDto dto) {
        return ResponseEntity.ok(personService.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PersonDto> update(@PathVariable Long id, @RequestBody PersonDto dto) {
        return ResponseEntity.ok(personService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        personService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
