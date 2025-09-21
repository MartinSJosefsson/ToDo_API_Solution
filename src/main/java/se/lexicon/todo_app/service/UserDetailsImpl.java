package se.lexicon.todo_app.service;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import se.lexicon.todo_app.entity.Role;
import se.lexicon.todo_app.entity.User;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class UserDetailsImpl implements UserDetails {

    private String username;

    @JsonIgnore
    private String password;

    private List<GrantedAuthority> authorities;

    private String firstName;
    private String lastName;
    private String email;

    public UserDetailsImpl(String username, String password, List<GrantedAuthority> authorities,
                           String firstName, String lastName, String email) {
        this.username = username;
        this.password = password;
        this.authorities = authorities;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public static UserDetailsImpl build(User user) {
        Set<Role> roles = user.getRoles();
        List<GrantedAuthority> authorities = roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.name()))
                .collect(Collectors.toList());

        return new UserDetailsImpl(
                user.getUsername(),
                user.getPassword(),
                authorities,
                user.getPerson() != null ? user.getPerson().getFirstName() : null,
                user.getPerson() != null ? user.getPerson().getLastName() : null,
                user.getPerson() != null ? user.getPerson().getEmail() : null
        );
    }

    public String getFirstName() {
        return firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public String getEmail() {
        return email;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }
    @Override
    public String getPassword() {
        return password;
    }
    @Override
    public String getUsername() {
        return username;
    }
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
    @Override
    public boolean isEnabled() {
        return true;
    }
}
