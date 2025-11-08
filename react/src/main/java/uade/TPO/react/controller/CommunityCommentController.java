package uade.TPO.react.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
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
//@CrossOrigin(origins = "*")
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
    public CommunityComment getCommentById(@PathVariable Long id) {
        return communityCommentService.getById(id);
    }

    // Crear un nuevo comentario para un post
    @PostMapping("/post/{postId}")
    public CommunityComment createComment(@PathVariable Long postId, @RequestBody CommunityComment comment) {
        return communityCommentService.create(postId, comment);
    }

    // Actualizar un comentario
    @PutMapping("/{id}")
    public CommunityComment updateComment(@PathVariable Long id, @RequestBody CommunityComment comment) {
        return communityCommentService.update(id, comment);
    }

    // Eliminar un comentario
    @DeleteMapping("/{id}")
    public void deleteComment(@PathVariable Long id) {
        communityCommentService.delete(id);
    }
}
