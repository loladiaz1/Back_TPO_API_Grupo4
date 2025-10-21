package uade.TPO.react.service;

import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import uade.TPO.react.entity.User;

@Service
public class AuthService {

    @Autowired
    private UserService userService;

    public User register(Map<String, String> body) {
        String name = body.get("name");
        String email = body.get("email");
        String password = body.get("password");
        
        if (name == null || email == null || password == null) {
            throw new IllegalArgumentException("Faltan datos");
        }
        
        User created = userService.register(name, email, password);
        created.setPassword(null);
        return created;
    }

    public User login(Map<String, String> body) {
        String email = body.get("email");
        String password = body.get("password");
        
        if (email == null || password == null) {
            throw new IllegalArgumentException("Faltan datos");
        }
        
        Optional<User> userOpt = userService.login(email, password);
        if (userOpt.isEmpty()) {
            throw new IllegalArgumentException("Credenciales inválidas");
        }
        
        User user = userOpt.get();
        user.setPassword(null);
        return user;
    }

    public User getCurrentUser(Long userId) {
        Optional<User> userOpt = userService.findById(userId);
        if (userOpt.isEmpty()) {
            throw new IllegalArgumentException("Usuario no encontrado");
        }
        
        User user = userOpt.get();
        user.setPassword(null);
        return user;
    }
}

