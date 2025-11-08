package uade.TPO.react.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import uade.TPO.react.dto.AuthResponse;
import uade.TPO.react.dto.LoginRequest;
import uade.TPO.react.dto.RegisterRequest;
import uade.TPO.react.entity.User;
import uade.TPO.react.service.AuthService;
import uade.TPO.react.service.UserService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
//@CrossOrigin(origins = "*")
public class AuthController {

    private static final String SESSION_USER_ID = "USER_ID";

    @Autowired
    private AuthService authService;

    @Autowired
    private UserService userService;

    // Registro: espera RegisterRequest { name, email, password }
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        User created = authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // Login: espera LoginRequest { email, password } y devuelve token JWT
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        AuthResponse response = authService.authenticate(request);
        return ResponseEntity.ok(response);
    }

    // Logout (opcional con JWT)
    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String token) {
        // Opción 1: El frontend simplemente borra el token → devolver mensaje.
        // Opción 2: Podés implementar un token blacklist si querés invalidarlo realmente.
        return ResponseEntity.ok("Sesión cerrada (token invalidado en cliente)");
    }

    // Listar todos los usuarios registrados (sin passwords)
    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers() {
        List<User> users = userService.getAll();
        users.forEach(u -> u.setPassword(null));
        return ResponseEntity.ok(users);
    }

    // Obtener el usuario autenticado (por token)
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(Authentication authentication) {
        if (authentication == null || authentication.getName() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No autenticado");
        }

        String email = authentication.getName();
        Optional<User> userOpt = userService.findByEmail(email);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuario no encontrado");
        }

        User user = userOpt.get();
        user.setPassword(null);
        return ResponseEntity.ok(user);
    }

    // Borrar usuario por id (requiere autenticación)
    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

