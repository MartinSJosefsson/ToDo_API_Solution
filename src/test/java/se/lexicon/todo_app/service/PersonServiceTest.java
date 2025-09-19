package se.lexicon.todo_app.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import se.lexicon.todo_app.dto.PersonDto;
import se.lexicon.todo_app.entity.Person;
import se.lexicon.todo_app.repository.PersonRepository;

import java.lang.reflect.Constructor;
import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class PersonServiceTest {

    @Mock
    private PersonRepository personRepository;

    @InjectMocks
    private PersonServiceImpl personService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindById() {
        // Arrange
        Person person = new Person();
        // set fields on the Person entity in a neutral way (adjust if your entity has different setters)
        try {
            // try common setter names; if your entity uses different names change accordingly
            person.getClass().getMethod("setFirstName", String.class).invoke(person, "Mehrdad");
            person.getClass().getMethod("setLastName", String.class).invoke(person, "Javan");
            person.getClass().getMethod("setEmail", String.class).invoke(person, "mehrdad@test.se");
        } catch (NoSuchMethodException e) {
            // If setters are named differently, try "setName" as fallback
            try {
                person.getClass().getMethod("setName", String.class).invoke(person, "Mehrdad Javan");
                person.getClass().getMethod("setEmail", String.class).invoke(person, "mehrdad@test.se");
            } catch (Exception ignored) {
                // If even that fails, tests still proceed; assertions below may need adaptation if fields are different.
            }
        } catch (Exception ex) {
            // ignore reflection invocation errors for test setup
        }

        when(personRepository.findById(1L)).thenReturn(Optional.of(person));

        // Act
        PersonDto result = personService.findById(1L);

        // Assert - use the record-style accessor names or methods depending on your PersonDto definition
        // Try common names; if your PersonDto has different accessors, change these assertions accordingly.
        if (result.getClass().getMethods() != null) {
            try {
                // try record style method names first (firstName/lastName/email)
                String firstName = (String) result.getClass().getMethod("firstName").invoke(result);
                String lastName = (String) result.getClass().getMethod("lastName").invoke(result);
                String email = (String) result.getClass().getMethod("email").invoke(result);

                assertThat(firstName).isEqualTo("Mehrdad");
                assertThat(lastName).isEqualTo("Javan");
                assertThat(email).isEqualTo("mehrdad@test.se");
                return;
            } catch (NoSuchMethodException ignored) {
                // fall through and try other accessor names
            } catch (Exception ex) {
                fail("Unable to introspect PersonDto: " + ex.getMessage());
            }
        }

        // Fallback assertions if the DTO uses getFirstName/getLastName/getEmail
        try {
            String firstName = (String) result.getClass().getMethod("getFirstName").invoke(result);
            String lastName = (String) result.getClass().getMethod("getLastName").invoke(result);
            String email = (String) result.getClass().getMethod("getEmail").invoke(result);

            assertThat(firstName).isEqualTo("Mehrdad");
            assertThat(lastName).isEqualTo("Javan");
            assertThat(email).isEqualTo("mehrdad@test.se");
        } catch (Exception ex) {
            // If we can't find matching accessors, fail the test with a helpful message
            fail("PersonDto does not expose expected accessors (firstName/firstName() or getFirstName()). " +
                    "Please update the test to match your DTO. Reflection error: " + ex.getMessage());
        }
    }

    @Test
    void testCreatePerson() {
        // We will instantiate the PersonRegistrationDto dynamically (reflection) so this test
        // works no matter what canonical constructor your record has (as long as it accepts Strings / Sets).
        Object registrationDto;
        try {
            Class<?> dtoClass = Class.forName("se.lexicon.todo_app.dto.PersonRegistrationDto");
            Constructor<?>[] ctors = dtoClass.getConstructors();

            if (ctors.length == 0) {
                fail("PersonRegistrationDto has no public constructors.");
                return;
            }

            // pick the first public constructor and build a sensible args array
            Constructor<?> chosen = ctors[0];
            Class<?>[] paramTypes = chosen.getParameterTypes();
            Object[] args = new Object[paramTypes.length];

            // fill args: Strings -> sample values; Set -> empty set; other types -> null
            for (int i = 0; i < paramTypes.length; i++) {
                Class<?> p = paramTypes[i];
                if (p.equals(String.class)) {
                    // supply sample values for first few string params
                    switch (i) {
                        case 0 -> args[i] = "Martin";
                        case 1 -> args[i] = "Josefsson";
                        case 2 -> args[i] = "martin@test.se";
                        case 3 -> args[i] = "mjosefsson";
                        case 4 -> args[i] = "password";
                        default -> args[i] = "value" + i;
                    }
                } else if (java.util.Set.class.isAssignableFrom(p)) {
                    args[i] = Collections.emptySet();
                } else {
                    args[i] = null;
                }
            }

            registrationDto = chosen.newInstance(args);
        } catch (ClassNotFoundException cnf) {
            fail("PersonRegistrationDto class not found: " + cnf.getMessage());
            return;
        } catch (Exception ex) {
            fail("Failed to construct PersonRegistrationDto via reflection: " + ex.getMessage());
            return;
        }

        // Prepare the saved entity result returned by repository
        Person savedPerson = new Person();
        try {
            savedPerson.getClass().getMethod("setFirstName", String.class).invoke(savedPerson, "Martin");
            savedPerson.getClass().getMethod("setLastName", String.class).invoke(savedPerson, "Josefsson");
            savedPerson.getClass().getMethod("setEmail", String.class).invoke(savedPerson, "martin@test.se");
        } catch (Exception ignored) {
            // ignore if setters differ
        }

        // Stub repository to return our savedPerson whenever save(any(Person.class)) is called
        when(personRepository.save(any(Person.class))).thenReturn(savedPerson);

        // Call service.create(...) using the DTO instance we built.
        PersonDto result;
        try {
            result = personService.create((se.lexicon.todo_app.dto.PersonRegistrationDto) registrationDto);
        } catch (ClassCastException cce) {
            fail("PersonRegistrationDto runtime type mismatch: " + cce.getMessage());
            return;
        } catch (Exception ex) {
            fail("personService.create threw an exception: " + ex.getMessage());
            return;
        }

        // Assert the result DTO fields (try record-style accessors then getters)
        try {
            String firstName = (String) result.getClass().getMethod("firstName").invoke(result);
            String lastName = (String) result.getClass().getMethod("lastName").invoke(result);
            String email = (String) result.getClass().getMethod("email").invoke(result);

            assertThat(firstName).isEqualTo("Martin");
            assertThat(lastName).isEqualTo("Josefsson");
            assertThat(email).isEqualTo("martin@test.se");
            return;
        } catch (NoSuchMethodException ignored) {
            // try getX style
        } catch (Exception ex) {
            fail("Unable to introspect PersonDto (record accessors) : " + ex.getMessage());
            return;
        }

        try {
            String firstName = (String) result.getClass().getMethod("getFirstName").invoke(result);
            String lastName = (String) result.getClass().getMethod("getLastName").invoke(result);
            String email = (String) result.getClass().getMethod("getEmail").invoke(result);

            assertThat(firstName).isEqualTo("Martin");
            assertThat(lastName).isEqualTo("Josefsson");
            assertThat(email).isEqualTo("martin@test.se");
        } catch (Exception ex) {
            fail("PersonDto does not expose expected accessors (firstName()/email() or getFirstName()/getEmail()). " +
                    "Please update the test to match your DTO. Reflection error: " + ex.getMessage());
        }
    }
}
