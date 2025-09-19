package se.lexicon.todo_app.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import se.lexicon.todo_app.dto.JwtResponse;
import se.lexicon.todo_app.dto.SignupRequest;
import se.lexicon.todo_app.entity.Person;
import se.lexicon.todo_app.entity.Role;
import se.lexicon.todo_app.entity.User;
import se.lexicon.todo_app.repository.PersonRepository;
import se.lexicon.todo_app.repository.UserRepository;
import se.lexicon.todo_app.security.JwtTokenUtil;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PersonRepository personRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenUtil;

    public AuthServiceImpl(AuthenticationManager authenticationManager,
                           UserRepository userRepository,
                           PersonRepository personRepository,
                           PasswordEncoder passwordEncoder,
                           JwtTokenUtil jwtTokenUtil) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.personRepository = personRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @Override
    public JwtResponse generateJwtResponse(Authentication authentication) {
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtTokenUtil.generateToken(authentication);

        // ✅ FIX: get principal
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        List<String> roles = userDetails.getAuthorities().stream()
                .map(grantedAuthority -> grantedAuthority.getAuthority())
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
            throw new RuntimeException("Username is already taken!");
        }

        // Create new User
        User user = new User();
        user.setUsername(signupRequest.getUsername());
        user.setPassword(passwordEncoder.encode(signupRequest.getPassword()));

        Set<Role> roles = signupRequest.getRoles();
        if (roles == null || roles.isEmpty()) {
            roles = Set.of(Role.USER);
        }
        user.setRoles(roles);

        userRepository.save(user);

        // Create related Person
        Person person = new Person();
        person.setFirstName(signupRequest.getFirstName());
        person.setLastName(signupRequest.getLastName());
        person.setEmail(signupRequest.getEmail());
        person.setUser(user);

        personRepository.save(person);

        return new JwtResponse(
                "",
                "Bearer",
                user.getUsername(),
                person.getFirstName(),
                person.getEmail(),
                roles.stream().map(Enum::name).toList()
        );
    }
}
