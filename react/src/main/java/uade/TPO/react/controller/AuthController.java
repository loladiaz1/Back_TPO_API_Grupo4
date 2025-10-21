package uade.TPO.react.controllers;

import java.util.Map;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import uade.TPO.react.entity.User;
import uade.TPO.react.service.AuthService;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private static final String SESSION_USER_ID = "USER_ID";

    @Autowired
    private AuthService authService;

    // Registro: espera { name, email, password }
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> body) {
        try {
            User created = authService.register(body);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
        }
    }

    // Login: espera { email, password }
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body, HttpSession session) {
        try {
            User user = authService.login(body);
            session.setAttribute(SESSION_USER_ID, user.getId());
            return ResponseEntity.ok(user);
        } catch (IllegalArgumentException ex) {
            if (ex.getMessage().equals("Credenciales inválidas")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
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
        
        try {
            Long id = (Long) idObj;
            User user = authService.getCurrentUser(id);
            return ResponseEntity.ok(user);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
        }
    }
}

