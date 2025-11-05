package uade.TPO.react.service;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import uade.TPO.react.dto.AuthResponse;
import uade.TPO.react.dto.LoginRequest;
import uade.TPO.react.dto.RegisterRequest;
import uade.TPO.react.entity.User;
import uade.TPO.react.util.JwtUtil;

@Service
public class AuthService {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    public User register(RegisterRequest request) {
        if (request.getName() == null || request.getEmail() == null || request.getPassword() == null) {
            throw new IllegalArgumentException("Faltan datos");
        }
        
        User created = userService.register(request.getName(), request.getEmail(), request.getPassword());
        created.setPassword(null);
        return created;
    }

    public AuthResponse authenticate(LoginRequest request) {
        if (request.getEmail() == null || request.getPassword() == null) {
            throw new IllegalArgumentException("Faltan datos");
        }
        
        try {
            // Usar AuthenticationManager de Spring Security
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );
            
            // Si la autenticación es exitosa, obtener el usuario
            User user = (User) authentication.getPrincipal();
            
            // Extraer roles
            Set<String> roles = user.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());
            
            // Generar token JWT
            String token = jwtUtil.generateToken(user.getEmail(), roles);
            
            // Retornar respuesta con token
            return new AuthResponse(token, user.getEmail(), user.getName());
        } catch (AuthenticationException e) {
            throw new IllegalArgumentException("Credenciales inválidas");
        }
    }
}

