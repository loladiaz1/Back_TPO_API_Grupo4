package uade.TPO.react.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import uade.TPO.react.dto.AuthResponse;
import uade.TPO.react.dto.LoginRequest;
import uade.TPO.react.dto.RegisterRequest;
import uade.TPO.react.entity.User;
import uade.TPO.react.service.AuthService;

@RestController
@RequestMapping("/api/auth")
//@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private AuthService authService;

    // Registro: espera RegisterRequest { name, email, password }
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        try {
            User created = authService.register(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
        }
    }

    // Login: espera LoginRequest { email, password } y devuelve token JWT
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            AuthResponse response = authService.authenticate(request);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
        }
    }
}

