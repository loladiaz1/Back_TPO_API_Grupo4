package uade.TPO.react.controllers;

import java.util.Map;
import java.util.Optional;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import uade.TPO.react.entity.User;
import uade.TPO.react.service.UserService;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private static final String SESSION_USER_ID = "USER_ID";

    @Autowired
    private UserService userService;

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

    // Login: espera { email, password }
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
        session.setAttribute(SESSION_USER_ID, user.getId());
        user.setPassword(null);
        return ResponseEntity.ok(user);
    }

    // Logout: invalida la sesión
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok().body("Sesión cerrada");
    }

    // Opcional: obtener usuario logueado
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
}

