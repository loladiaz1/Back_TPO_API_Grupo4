package uade.TPO.react.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import uade.TPO.react.entity.Role;
import uade.TPO.react.entity.User;
import uade.TPO.react.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public User register(String name, String email, String rawPassword) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("Email already in use");
        }
        String hashed = passwordEncoder.encode(rawPassword);
        User user = new User(name, email, hashed);
        user.setRole(Role.USER); // Asignar rol USER por defecto
        return userRepository.save(user);
    }

    public Optional<User> login(String email, String rawPassword) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) return Optional.empty();
        User user = userOpt.get();
        if (passwordEncoder.matches(rawPassword, user.getPassword())) {
            return Optional.of(user);
        }
        return Optional.empty();
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }
}

