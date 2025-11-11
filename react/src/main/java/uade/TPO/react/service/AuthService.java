package uade.TPO.react.service;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import uade.TPO.react.dto.AuthResponse;
import uade.TPO.react.dto.LoginRequest;
import uade.TPO.react.dto.RegisterRequest;
import uade.TPO.react.dto.UserDTO;
import uade.TPO.react.entity.User;
import uade.TPO.react.exception.ValidationException;
import uade.TPO.react.util.JwtUtil;

@Service
public class AuthService {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;


    public UserDTO register(RegisterRequest request) {
        if (request == null) {
            throw new ValidationException("La solicitud no puede ser nula");
        }
        if (request.getName() == null || request.getName().trim().isEmpty()) {
            throw new ValidationException("El nombre es obligatorio");
        }
        if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
            throw new ValidationException("El email es obligatorio");
        }
        if (request.getPassword() == null || request.getPassword().trim().isEmpty()) {
            throw new ValidationException("La contraseña es obligatoria");
        }
        
        return userService.register(request.getName(), request.getEmail(), request.getPassword());
    }

    public AuthResponse authenticate(LoginRequest request) {
        if (request == null) {
            throw new ValidationException("La solicitud no puede ser nula");
        }
        if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
            throw new ValidationException("El email es obligatorio");
        }
        if (request.getPassword() == null || request.getPassword().trim().isEmpty()) {
            throw new ValidationException("La contraseña es obligatoria");
        }
        
        // Usar AuthenticationManager de Spring Security
        // Si falla, AuthenticationException se propagará y será manejada
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );
        } catch (BadCredentialsException e) {
            throw new ValidationException("Credenciales inválidas");
        } catch (AuthenticationException e) {
            throw new ValidationException("Error de autenticación: " + e.getMessage());
        }
        
        // Si la autenticación es exitosa, obtener el usuario
        User user = (User) authentication.getPrincipal();
        
        // Extraer roles
        Set<String> roles = user.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.toSet());
        
        // Generar token JWT
        String token = jwtUtil.generateToken(user.getEmail(), roles);
        
        // Obtener el rol del usuario (sin el prefijo ROLE_)
        String userRole = user.getRole() != null ? user.getRole().name() : "USER";
        
        // Retornar respuesta con token y rol
        return new AuthResponse(token, user.getEmail(), user.getName(), userRole);
    }
}

