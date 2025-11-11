package uade.TPO.react.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import uade.TPO.react.dto.CommentDTO;
import uade.TPO.react.service.CommentService;

@RestController
@RequestMapping("/comments")
//@CrossOrigin(origins = "*")
public class CommentController {

    @Autowired
    private CommentService commentService;

    // Obtener todos los comentarios
    @GetMapping
    public List<CommentDTO> getAllComments() {
        return commentService.getAll();
    }

    // Obtener comentarios de un juego específico
    @GetMapping("/game/{gameId}")
    public List<CommentDTO> getCommentsByGame(@PathVariable Long gameId) {
        return commentService.getByGameId(gameId);
    }

    // Obtener un comentario por ID
    @GetMapping("/{id}")
    public CommentDTO getCommentById(@PathVariable Long id) {
        return commentService.getById(id);
    }

    // Crear un nuevo comentario para un juego
    @PostMapping("/game/{gameId}")
    public CommentDTO createComment(@PathVariable Long gameId, @RequestBody CommentDTO comment) {
        return commentService.create(gameId, comment);
    }

    // Actualizar un comentario
    @PutMapping("/{id}")
    public CommentDTO updateComment(@PathVariable Long id, @RequestBody CommentDTO comment) {
        return commentService.update(id, comment);
    }

    // Eliminar un comentario
    @DeleteMapping("/{id}")
    public void deleteComment(@PathVariable Long id) {
        commentService.delete(id);
    }

    // Obtener estadísticas de comentarios de un juego
    @GetMapping("/game/{gameId}/stats")
    public Map<String, Object> getGameCommentStats(@PathVariable Long gameId) {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalComments", commentService.countByGameId(gameId));
        stats.put("averageRating", commentService.getAverageRating(gameId));
        return stats;
    }
}
