package se.lexicon.todo_app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class JwtResponse {
    private String token;
    private String type; // "Bearer"
    private String username;
    private String firstName;
    private String email;
    private List<String> roles;
}
