package uade.TPO.react.service;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import uade.TPO.react.entity.User;
import uade.TPO.react.repository.UserRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    public UserServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegisterSuccess() {
        String name = "Test";
        String email = "test@mail.com";
        String rawPassword = "1234";
        String hashed = "hashed";

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());
        when(passwordEncoder.encode(rawPassword)).thenReturn(hashed);
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArgument(0));

        User user = userService.register(name, email, rawPassword);

        assertEquals(name, user.getName());
        assertEquals(email, user.getEmail());
        assertNotEquals(rawPassword, user.getPassword());
    }

    @Test
    void testLoginSuccess() {
        String email = "test@mail.com";
        String rawPassword = "1234";
        String hashed = "hashed";
        User user = new User("Test", email, hashed);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(rawPassword, hashed)).thenReturn(true);

        Optional<User> result = userService.login(email, rawPassword);

        assertTrue(result.isPresent());
        assertEquals(email, result.get().getEmail());
    }
}
