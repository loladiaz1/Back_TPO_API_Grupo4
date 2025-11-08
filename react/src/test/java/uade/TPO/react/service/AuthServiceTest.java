package uade.TPO.react.service;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import uade.TPO.react.dto.AuthResponse;
import uade.TPO.react.dto.LoginRequest;
import uade.TPO.react.dto.RegisterRequest;
import uade.TPO.react.entity.User;
import uade.TPO.react.repository.UserRepository;
import uade.TPO.react.util.JwtUtil;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthServiceTest {

    @Mock
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AuthService authService;

    public AuthServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegisterSuccess() {
        RegisterRequest req = new RegisterRequest("Test", "test@mail.com", "1234");
        User user = new User("Test", "test@mail.com", "hashed");

        when(userService.register(anyString(), anyString(), anyString())).thenReturn(user);

        User result = authService.register(req);

        assertEquals("Test", result.getName());
        assertNull(result.getPassword());
    }

    @Test
    void testAuthenticateSuccess() {
        LoginRequest req = new LoginRequest("test@mail.com", "1234");
        User user = new User("Test", "test@mail.com", "hashed");
        Authentication auth = mock(Authentication.class);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(auth);
        when(auth.getPrincipal()).thenReturn(user);
        when(jwtUtil.generateToken(anyString(), anySet())).thenReturn("token");

        AuthResponse response = authService.authenticate(req);

        assertEquals("token", response.getToken());
        assertEquals("test@mail.com", response.getEmail());
    }
}
