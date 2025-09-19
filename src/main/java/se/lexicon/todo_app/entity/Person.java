package se.lexicon.todo_app.entity;

import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "persons")
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;
    private String email;

    @OneToOne
    @JoinColumn(name = "username", referencedColumnName = "username")
    private User user;

    public Person(String firstName, String lastName, String email, User user) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.user = user;
    }
}
