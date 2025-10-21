package uade.TPO.react.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import uade.TPO.react.entity.Comment;
import uade.TPO.react.service.CommentService;

@RestController
@RequestMapping("/comments")
@CrossOrigin(origins = "*")
public class CommentController {

    @Autowired
    private CommentService commentService;

    // Obtener todos los comentarios
    @GetMapping
    public List<Comment> getAllComments() {
        return commentService.getAll();
    }

    // Obtener comentarios de un juego específico
    @GetMapping("/game/{gameId}")
    public List<Comment> getCommentsByGame(@PathVariable Long gameId) {
        return commentService.getByGameId(gameId);
    }

    // Obtener un comentario por ID
    @GetMapping("/{id}")
    public ResponseEntity<Comment> getCommentById(@PathVariable Long id) {
        Comment comment = commentService.getById(id);
        if (comment == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(comment);
    }

    // Crear un nuevo comentario para un juego
    @PostMapping("/game/{gameId}")
    public ResponseEntity<?> createComment(@PathVariable Long gameId, @RequestBody Comment comment) {
        try {
            Comment created = commentService.create(gameId, comment);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // Actualizar un comentario
    @PutMapping("/{id}")
    public ResponseEntity<?> updateComment(@PathVariable Long id, @RequestBody Comment comment) {
        try {
            Comment updated = commentService.update(id, comment);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // Eliminar un comentario
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long id) {
        commentService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // Obtener estadísticas de comentarios de un juego
    @GetMapping("/game/{gameId}/stats")
    public ResponseEntity<Map<String, Object>> getGameCommentStats(@PathVariable Long gameId) {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalComments", commentService.countByGameId(gameId));
        stats.put("averageRating", commentService.getAverageRating(gameId));
        return ResponseEntity.ok(stats);
    }
}
