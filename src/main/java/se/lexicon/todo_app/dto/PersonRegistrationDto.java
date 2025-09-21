package se.lexicon.todo_app.dto;

public record PersonRegistrationDto(
        String firstName,
        String lastName,
        String email,
        String username
) {}
