package uade.TPO.react.service;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import uade.TPO.react.dto.UserDTO;
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
        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArgument(0));

        UserDTO user = userService.register(name, email, rawPassword);

        assertEquals(name, user.getName());
        assertEquals(email, user.getEmail());
        verify(passwordEncoder).encode(rawPassword);
        verify(userRepository).save(captor.capture());
        assertEquals(hashed, captor.getValue().getPassword());
    }

    @Test
    void testLoginSuccess() {
        String email = "test@mail.com";
        String rawPassword = "1234";
        String hashed = "hashed";
        User user = new User("Test", email, hashed);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(rawPassword, hashed)).thenReturn(true);

        Optional<UserDTO> result = userService.login(email, rawPassword);

        assertTrue(result.isPresent());
        assertEquals(email, result.get().getEmail());
    }
}
