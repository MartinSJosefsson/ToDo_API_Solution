package se.lexicon.todo_app.service;

import org.springframework.security.core.Authentication;
import se.lexicon.todo_app.dto.JwtResponse;
import se.lexicon.todo_app.dto.SignupRequest;

public interface AuthService {
    JwtResponse generateJwtResponse(Authentication authentication);
    JwtResponse registerUser(SignupRequest signupRequest);
}
