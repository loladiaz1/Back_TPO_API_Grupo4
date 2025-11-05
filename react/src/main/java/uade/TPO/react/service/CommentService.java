package uade.TPO.react.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import uade.TPO.react.entity.Comment;
import uade.TPO.react.entity.Game;
import uade.TPO.react.repository.CommentRepository;
import uade.TPO.react.repository.GameRepository;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private GameRepository gameRepository;

    // Obtener todos los comentarios
    public List<Comment> getAll() {
        return commentRepository.findAll();
    }

    // Obtener comentarios por ID de juego
    public List<Comment> getByGameId(Long gameId) {
        return commentRepository.findByGameId(gameId);
    }

    // Obtener un comentario por ID
    public Comment getById(Long id) {
        return commentRepository.findById(id).orElse(null);
    }

    // Crear un nuevo comentario
    public Comment create(Long gameId, Comment comment) {
        Game game = gameRepository.findById(gameId).orElse(null);
        if (game == null) {
            throw new RuntimeException("Juego no encontrado con ID: " + gameId);
        }
        
        // Validar rating (debe estar entre 1 y 5)
        if (comment.getRating() < 1 || comment.getRating() > 5) {
            throw new RuntimeException("El rating debe estar entre 1 y 5");
        }
        
        comment.setGame(game);
        return commentRepository.save(comment);
    }

    // Actualizar un comentario
    public Comment update(Long id, Comment updatedComment) {
        Comment existing = commentRepository.findById(id).orElse(null);
        if (existing == null) {
            throw new RuntimeException("Comentario no encontrado con ID: " + id);
        }
        
        // Validar rating si se está actualizando
        if (updatedComment.getRating() != null && 
            (updatedComment.getRating() < 1 || updatedComment.getRating() > 5)) {
            throw new RuntimeException("El rating debe estar entre 1 y 5");
        }
        
        if (updatedComment.getContent() != null) {
            existing.setContent(updatedComment.getContent());
        }
        if (updatedComment.getRating() != null) {
            existing.setRating(updatedComment.getRating());
        }
        
        return commentRepository.save(existing);
    }

    // Eliminar un comentario
    public void delete(Long id) {
        commentRepository.deleteById(id);
    }

    // Calcular promedio de calificaciones de un juego
    public Double getAverageRating(Long gameId) {
        List<Comment> comments = commentRepository.findByGameId(gameId);
        if (comments.isEmpty()) {
            return 0.0;
        }
        
        double sum = comments.stream()
            .mapToInt(Comment::getRating)
            .sum();
        
        return sum / comments.size();
    }

    // Contar comentarios de un juego
    public long countByGameId(Long gameId) {
        return commentRepository.countByGameId(gameId);
    }

    // Obtener estadísticas de comentarios de un juego
    public Map<String, Object> getGameCommentStats(Long gameId) {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalComments", countByGameId(gameId));
        stats.put("averageRating", getAverageRating(gameId));
        return stats;
    }
}
