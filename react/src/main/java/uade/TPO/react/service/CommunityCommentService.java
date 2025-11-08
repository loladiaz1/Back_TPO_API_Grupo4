package uade.TPO.react.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import uade.TPO.react.entity.CommunityComment;
import uade.TPO.react.entity.CommunityPost;
import uade.TPO.react.exception.ResourceNotFoundException;
import uade.TPO.react.exception.ValidationException;
import uade.TPO.react.repository.CommunityCommentRepository;
import uade.TPO.react.repository.CommunityPostRepository;

@Service
public class CommunityCommentService {

    @Autowired
    private CommunityCommentRepository communityCommentRepository;

    @Autowired
    private CommunityPostRepository communityPostRepository;

    // Obtener todos los comentarios
    public List<CommunityComment> getAll() {
        return communityCommentRepository.findAll();
    }

    // Obtener comentarios por ID de post
    public List<CommunityComment> getByPostId(Long postId) {
        if (postId == null) {
            throw new ValidationException("El ID del post no puede ser nulo");
        }
        return communityCommentRepository.findByPostId(postId);
    }

    // Obtener un comentario por ID
    public CommunityComment getById(Long id) {
        if (id == null) {
            throw new ValidationException("El ID no puede ser nulo");
        }
        return communityCommentRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Comentario no encontrado con ID: " + id));
    }

    // Crear un nuevo comentario
    public CommunityComment create(Long postId, CommunityComment comment) {
        if (postId == null) {
            throw new ValidationException("El ID del post no puede ser nulo");
        }
        if (comment == null) {
            throw new ValidationException("El comentario no puede ser nulo");
        }
        if (comment.getContent() == null || comment.getContent().trim().isEmpty()) {
            throw new ValidationException("El contenido del comentario es obligatorio");
        }
        
        CommunityPost post = communityPostRepository.findById(postId)
            .orElseThrow(() -> new ResourceNotFoundException("Post no encontrado con ID: " + postId));
        
        comment.setPost(post);
        return communityCommentRepository.save(comment);
    }

    // Actualizar un comentario
    public CommunityComment update(Long id, CommunityComment updatedComment) {
        if (id == null) {
            throw new ValidationException("El ID no puede ser nulo");
        }
        if (updatedComment == null) {
            throw new ValidationException("El comentario no puede ser nulo");
        }
        
        CommunityComment existing = communityCommentRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Comentario no encontrado con ID: " + id));
        
        if (updatedComment.getContent() != null) {
            if (updatedComment.getContent().trim().isEmpty()) {
                throw new ValidationException("El contenido del comentario no puede estar vacío");
            }
            existing.setContent(updatedComment.getContent());
        }
        
        return communityCommentRepository.save(existing);
    }

    // Eliminar un comentario
    public void delete(Long id) {
        if (id == null) {
            throw new ValidationException("El ID no puede ser nulo");
        }
        if (!communityCommentRepository.existsById(id)) {
            throw new ResourceNotFoundException("Comentario no encontrado con ID: " + id);
        }
        communityCommentRepository.deleteById(id);
    }
}
