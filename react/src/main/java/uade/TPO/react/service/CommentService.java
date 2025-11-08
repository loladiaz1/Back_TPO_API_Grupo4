package uade.TPO.react.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import uade.TPO.react.entity.Comment;
import uade.TPO.react.entity.Game;
import uade.TPO.react.exception.ResourceNotFoundException;
import uade.TPO.react.exception.ValidationException;
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
        if (gameId == null) {
            throw new ValidationException("El ID del juego no puede ser nulo");
        }
        return commentRepository.findByGameId(gameId);
    }

    // Obtener un comentario por ID
    public Comment getById(Long id) {
        if (id == null) {
            throw new ValidationException("El ID no puede ser nulo");
        }
        return commentRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Comentario no encontrado con ID: " + id));
    }

    // Crear un nuevo comentario
    public Comment create(Long gameId, Comment comment) {
        if (gameId == null) {
            throw new ValidationException("El ID del juego no puede ser nulo");
        }
        if (comment == null) {
            throw new ValidationException("El comentario no puede ser nulo");
        }
        
        Game game = gameRepository.findById(gameId)
            .orElseThrow(() -> new ResourceNotFoundException("Juego no encontrado con ID: " + gameId));
        
        // Validar rating (debe estar entre 1 y 5)
        if (comment.getRating() == null) {
            throw new ValidationException("El rating es obligatorio");
        }
        if (comment.getRating() < 1 || comment.getRating() > 5) {
            throw new ValidationException("El rating debe estar entre 1 y 5");
        }
        
        comment.setGame(game);
        return commentRepository.save(comment);
    }

    // Actualizar un comentario
    public Comment update(Long id, Comment updatedComment) {
        if (id == null) {
            throw new ValidationException("El ID no puede ser nulo");
        }
        if (updatedComment == null) {
            throw new ValidationException("El comentario no puede ser nulo");
        }
        
        Comment existing = commentRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Comentario no encontrado con ID: " + id));
        
        // Validar rating si se está actualizando
        if (updatedComment.getRating() != null && 
            (updatedComment.getRating() < 1 || updatedComment.getRating() > 5)) {
            throw new ValidationException("El rating debe estar entre 1 y 5");
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
        if (id == null) {
            throw new ValidationException("El ID no puede ser nulo");
        }
        if (!commentRepository.existsById(id)) {
            throw new ResourceNotFoundException("Comentario no encontrado con ID: " + id);
        }
        commentRepository.deleteById(id);
    }

    // Calcular promedio de calificaciones de un juego
    public Double getAverageRating(Long gameId) {
        if (gameId == null) {
            throw new ValidationException("El ID del juego no puede ser nulo");
        }
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
        if (gameId == null) {
            throw new ValidationException("El ID del juego no puede ser nulo");
        }
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
