package uade.TPO.react.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import uade.TPO.react.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
