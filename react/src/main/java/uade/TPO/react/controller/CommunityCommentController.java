package uade.TPO.react.controllers;

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

import uade.TPO.react.entity.CommunityComment;
import uade.TPO.react.service.CommunityCommentService;

@RestController
@RequestMapping("/community/comments")
@CrossOrigin(origins = "*")
public class CommunityCommentController {

    @Autowired
    private CommunityCommentService communityCommentService;

    // Obtener todos los comentarios
    @GetMapping
    public List<CommunityComment> getAllComments() {
        return communityCommentService.getAll();
    }

    // Obtener comentarios de un post específico
    @GetMapping("/post/{postId}")
    public List<CommunityComment> getCommentsByPost(@PathVariable Long postId) {
        return communityCommentService.getByPostId(postId);
    }

    // Obtener un comentario por ID
    @GetMapping("/{id}")
    public ResponseEntity<CommunityComment> getCommentById(@PathVariable Long id) {
        CommunityComment comment = communityCommentService.getById(id);
        if (comment == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(comment);
    }

    // Crear un nuevo comentario para un post
    @PostMapping("/post/{postId}")
    public ResponseEntity<?> createComment(@PathVariable Long postId, @RequestBody CommunityComment comment) {
        try {
            CommunityComment created = communityCommentService.create(postId, comment);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // Actualizar un comentario
    @PutMapping("/{id}")
    public ResponseEntity<?> updateComment(@PathVariable Long id, @RequestBody CommunityComment comment) {
        try {
            CommunityComment updated = communityCommentService.update(id, comment);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // Eliminar un comentario
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long id) {
        communityCommentService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
