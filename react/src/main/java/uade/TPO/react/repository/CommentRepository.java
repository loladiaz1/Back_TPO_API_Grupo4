package uade.TPO.react.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import uade.TPO.react.entity.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    
    // Encontrar todos los comentarios de un juego específico
    List<Comment> findByGameId(Long gameId);
    
    // Encontrar comentarios por nombre de usuario
    List<Comment> findByUserName(String userName);
    
    // Contar comentarios de un juego
    long countByGameId(Long gameId);
}
