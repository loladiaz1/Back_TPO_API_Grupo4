package uade.TPO.react.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import uade.TPO.react.entity.CommunityComment;
import uade.TPO.react.entity.CommunityPost;
import uade.TPO.react.exception.ResourceNotFoundException;
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
        return communityCommentRepository.findByPostId(postId);
    }

    // Obtener un comentario por ID
    public CommunityComment getById(Long id) {
        return communityCommentRepository.findById(id).orElse(null);
    }

    // Crear un nuevo comentario
    public CommunityComment create(Long postId, CommunityComment comment) {
        CommunityPost post = communityPostRepository.findById(postId).orElse(null);
        if (post == null) {
            throw new ResourceNotFoundException("Post no encontrado con ID: " + postId);
        }
        
        comment.setPost(post);
        return communityCommentRepository.save(comment);
    }

    // Actualizar un comentario
    public CommunityComment update(Long id, CommunityComment updatedComment) {
        CommunityComment existing = communityCommentRepository.findById(id).orElse(null);
        if (existing == null) {
            throw new ResourceNotFoundException("Comentario no encontrado con ID: " + id);
        }
        
        if (updatedComment.getContent() != null) {
            existing.setContent(updatedComment.getContent());
        }
        
        return communityCommentRepository.save(existing);
    }

    // Eliminar un comentario
    public void delete(Long id) {
        communityCommentRepository.deleteById(id);
    }
}
