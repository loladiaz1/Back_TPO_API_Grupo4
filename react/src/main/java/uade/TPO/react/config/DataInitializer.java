package uade.TPO.react.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import uade.TPO.react.entity.Role;
import uade.TPO.react.entity.User;
import uade.TPO.react.repository.UserRepository;

/**
 * Componente que inicializa datos por defecto en la base de datos
 * Se ejecuta automáticamente al iniciar la aplicación
 */
@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Verificar si ya existe un usuario admin
        if (userRepository.findByEmail("admin@admin").isEmpty()) {
            // Crear usuario admin por defecto
            User admin = new User();
            admin.setName("Administrador");
            admin.setEmail("admin@admin");
            admin.setPassword(passwordEncoder.encode("admin")); // Contraseña: admin
            admin.setRole(Role.ADMIN);
            
            userRepository.save(admin);
            System.out.println("✅ Usuario admin creado exitosamente");
            System.out.println("   Email: admin@admin");
            System.out.println("   Password: admin");
        } else {
            System.out.println("ℹ️  Usuario admin ya existe en la base de datos");
        }
    }
}
