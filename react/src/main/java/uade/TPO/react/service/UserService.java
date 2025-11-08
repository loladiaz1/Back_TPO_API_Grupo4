package uade.TPO.react.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import uade.TPO.react.entity.Role;
import uade.TPO.react.entity.User;
import uade.TPO.react.exception.ConflictException;
import uade.TPO.react.exception.ResourceNotFoundException;
import uade.TPO.react.exception.ValidationException;
import uade.TPO.react.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public User register(String name, String email, String rawPassword) {
        if (name == null || name.trim().isEmpty()) {
            throw new ValidationException("El nombre es obligatorio");
        }
        if (email == null || email.trim().isEmpty()) {
            throw new ValidationException("El email es obligatorio");
        }
        if (rawPassword == null || rawPassword.trim().isEmpty()) {
            throw new ValidationException("La contraseña es obligatoria");
        }
        if (rawPassword.length() < 6) {
            throw new ValidationException("La contraseña debe tener al menos 6 caracteres");
        }
        if (userRepository.findByEmail(email).isPresent()) {
            throw new ConflictException("El email ya está en uso");
        }
        String hashed = passwordEncoder.encode(rawPassword);
        User user = new User(name, email, hashed);
        user.setRole(Role.USER); // Asignar rol USER por defecto
        return userRepository.save(user);
    }

    public Optional<User> login(String email, String rawPassword) {
        if (email == null || email.trim().isEmpty()) {
            throw new ValidationException("El email no puede ser nulo o vacío");
        }
        if (rawPassword == null || rawPassword.trim().isEmpty()) {
            throw new ValidationException("La contraseña no puede ser nula o vacía");
        }
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) return Optional.empty();
        User user = userOpt.get();
        if (passwordEncoder.matches(rawPassword, user.getPassword())) {
            return Optional.of(user);
        }
        return Optional.empty();
    }

    public Optional<User> findById(Long id) {
        if (id == null) {
            throw new ValidationException("El ID no puede ser nulo");
        }
        return userRepository.findById(id);
    }

    // buscar usuario por email
    public Optional<User> findByEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new ValidationException("El email no puede ser nulo o vacío");
        }
        return userRepository.findByEmail(email);
    }

    // Obtener todos los usuarios
    public List<User> getAll() {
        return userRepository.findAll();
    }

    // Borrar usuario por id
    public void delete(Long id) {
        if (id == null) {
            throw new ValidationException("El ID no puede ser nulo");
        }
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("Usuario no encontrado con ID: " + id);
        }
        userRepository.deleteById(id);
    }
}

