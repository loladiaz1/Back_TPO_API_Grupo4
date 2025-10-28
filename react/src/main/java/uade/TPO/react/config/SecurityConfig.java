package uade.TPO.react.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import uade.TPO.react.filter.JwtFilter;
import uade.TPO.react.repository.UserRepository;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtFilter jwtFilter;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> userRepository.findByEmail(username)
            .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado: " + username));
    }

    @Bean
    public AuthenticationManager authenticationManager(
            UserDetailsService userDetailsService,
            BCryptPasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder);

        return new ProviderManager(authenticationProvider);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // Deshabilitar CSRF para APIs REST
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Sin sesiones (stateless con JWT)
            )
            .authorizeHttpRequests(auth -> auth
                // Endpoints públicos
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/games/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/gametypes/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/comments/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/community-posts/**").permitAll()
                
                // Endpoints para administradores (si decides crearlos)
                .requestMatchers("/api/admin/**").hasRole("ADMIN")
                
                // Endpoints que requieren autenticación (POST, PUT, DELETE)
                .requestMatchers(HttpMethod.POST, "/api/games/**").authenticated()
                .requestMatchers(HttpMethod.PUT, "/api/games/**").authenticated()
                .requestMatchers(HttpMethod.DELETE, "/api/games/**").authenticated()
                
                .requestMatchers(HttpMethod.POST, "/api/comments/**").authenticated()
                .requestMatchers(HttpMethod.PUT, "/api/comments/**").authenticated()
                .requestMatchers(HttpMethod.DELETE, "/api/comments/**").authenticated()
                
                .requestMatchers(HttpMethod.POST, "/api/community-posts/**").authenticated()
                .requestMatchers(HttpMethod.PUT, "/api/community-posts/**").authenticated()
                .requestMatchers(HttpMethod.DELETE, "/api/community-posts/**").authenticated()
                
                // Cualquier otra petición requiere autenticación
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class); // Agregar filtro JWT
        
        return http.build();
    }
}
