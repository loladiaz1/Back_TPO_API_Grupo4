package uade.TPO.react.controllers;

import java.util.List;

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

import uade.TPO.react.dto.CommunityCommentDTO;
import uade.TPO.react.service.CommunityCommentService;

@RestController
@RequestMapping("/community/comments")
//@CrossOrigin(origins = "*")
public class CommunityCommentController {

    @Autowired
    private CommunityCommentService communityCommentService;

    // Obtener todos los comentarios
    @GetMapping
    public List<CommunityCommentDTO> getAllComments() {
        return communityCommentService.getAll();
    }

    // Obtener comentarios de un post específico
    @GetMapping("/post/{postId}")
    public List<CommunityCommentDTO> getCommentsByPost(@PathVariable Long postId) {
        return communityCommentService.getByPostId(postId);
    }

    // Obtener un comentario por ID
    @GetMapping("/{id}")
    public CommunityCommentDTO getCommentById(@PathVariable Long id) {
        return communityCommentService.getById(id);
    }

    // Crear un nuevo comentario para un post
    @PostMapping("/post/{postId}")
    public CommunityCommentDTO createComment(@PathVariable Long postId, @RequestBody CommunityCommentDTO comment) {
        return communityCommentService.create(postId, comment);
    }

    // Actualizar un comentario
    @PutMapping("/{id}")
    public CommunityCommentDTO updateComment(@PathVariable Long id, @RequestBody CommunityCommentDTO comment) {
        return communityCommentService.update(id, comment);
    }

    // Eliminar un comentario
    @DeleteMapping("/{id}")
    public void deleteComment(@PathVariable Long id) {
        communityCommentService.delete(id);
    }
}
