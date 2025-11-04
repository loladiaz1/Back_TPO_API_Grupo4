package uade.TPO.react.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import uade.TPO.react.config.JwtTokenProvider;
import uade.TPO.react.entity.User;
import uade.TPO.react.service.UserService;

@RestController
@RequestMapping("/auth")
//@CrossOrigin(origins = "*")
public class AuthController {

    private static final String SESSION_USER_ID = "USER_ID";

    @Autowired
    private UserService userService;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    // Registro: espera { name, email, password }
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> body) {
        String name = body.get("name");
        String email = body.get("email");
        String password = body.get("password");
        if (name == null || email == null || password == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Faltan datos");
        }
        try {
            User created = userService.register(name, email, password);
            created.setPassword(null);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
        }
    }


    // Login: espera { email, password } - devuelve user + token
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body, HttpSession session) {
        String email = body.get("email");
        String password = body.get("password");
        if (email == null || password == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Faltan datos");
        }
        Optional<User> userOpt = userService.login(email, password);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales inválidas");
        }
        User user = userOpt.get();
        session.setAttribute(SESSION_USER_ID, user.getId()); // opcional, por compatibilidad
        user.setPassword(null);

        String token = jwtTokenProvider.generateToken(user.getEmail());
        Map<String, Object> resp = new HashMap<>();
        resp.put("token", token);
        resp.put("user", user);

        return ResponseEntity.ok(resp);
    }

    // Logout: invalida la sesión
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok().body("Sesión cerrada");
    }

    // Listar todos los usuarios registrados (sin passwords)
    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers() {
        List<User> users = userService.getAll();
        users.forEach(u -> u.setPassword(null));
        return ResponseEntity.ok(users);
    }

    // Obtener usuario logueado
    @GetMapping("/me")
    public ResponseEntity<?> me(HttpSession session) {
        Object idObj = session.getAttribute(SESSION_USER_ID);
        if (idObj == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No autenticado");
        Long id = (Long) idObj;
        Optional<User> userOpt = userService.findById(id);
        if (userOpt.isEmpty()) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuario no encontrado");
        User user = userOpt.get();
        user.setPassword(null);
        return ResponseEntity.ok(user);
    }

    // Borrar usuario por id (requiere autenticación)
    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        boolean deleted = userService.delete(id);
        if (!deleted) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado");
        }
        return ResponseEntity.noContent().build();
    }
}

