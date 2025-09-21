package se.lexicon.todo_app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import se.lexicon.todo_app.dto.JwtResponse;
import se.lexicon.todo_app.dto.SignupRequest;
import se.lexicon.todo_app.entity.Role;
import se.lexicon.todo_app.entity.User;
import se.lexicon.todo_app.repository.UserRepository;
import se.lexicon.todo_app.security.JwtTokenUtil;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthServiceImpl(AuthenticationManager authenticationManager,
                           JwtTokenUtil jwtTokenUtil,
                           UserRepository userRepository,
                           PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public JwtResponse generateJwtResponse(Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        String jwt = jwtTokenUtil.generateToken(userDetails);

        List<String> roles = userDetails.getAuthorities().stream()
                .map(auth -> auth.getAuthority())
                .collect(Collectors.toList());

        return new JwtResponse(
                jwt,
                "Bearer",
                userDetails.getUsername(),
                userDetails.getFirstName(),
                userDetails.getEmail(),
                roles
        );
    }

    @Override
    public JwtResponse registerUser(SignupRequest signupRequest) {
        if (userRepository.existsById(signupRequest.getUsername())) {
            throw new IllegalArgumentException("Username is already taken");
        }

        User user = new User(
                signupRequest.getUsername(),
                passwordEncoder.encode(signupRequest.getPassword())
        );

        if (signupRequest.getRoles() == null || signupRequest.getRoles().isEmpty()) {
            user.addRole(Role.USER);
        } else {
            signupRequest.getRoles().forEach(user::addRole);
        }

        userRepository.save(user);

        // authenticate immediately after registration
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(signupRequest.getUsername(), signupRequest.getPassword())
        );

        return generateJwtResponse(authentication);
    }
}
