package uade.TPO.react.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import uade.TPO.react.dto.CommunityCommentDTO;
import uade.TPO.react.exception.ResourceNotFoundException;
import uade.TPO.react.exception.ValidationException;
import uade.TPO.react.repository.CommunityCommentRepository;
import uade.TPO.react.repository.CommunityPostRepository;
import uade.TPO.react.entity.CommunityComment;
import uade.TPO.react.entity.CommunityPost;

@Service
public class CommunityCommentService {

    @Autowired
    private CommunityCommentRepository communityCommentRepository;

    @Autowired
    private CommunityPostRepository communityPostRepository;

    // Obtener todos los comentarios
    public List<CommunityCommentDTO> getAll() {
        return communityCommentRepository.findAll().stream()
            .map(this::toDto)
            .collect(Collectors.toList());
    }

    // Obtener comentarios por ID de post
    public List<CommunityCommentDTO> getByPostId(Long postId) {
        if (postId == null) {
            throw new ValidationException("El ID del post no puede ser nulo");
        }
        return communityCommentRepository.findByPostId(postId).stream()
            .map(this::toDto)
            .collect(Collectors.toList());
    }

    // Obtener un comentario por ID
    public CommunityCommentDTO getById(Long id) {
        if (id == null) {
            throw new ValidationException("El ID no puede ser nulo");
        }
        CommunityComment comment = communityCommentRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Comentario no encontrado con ID: " + id));
        return toDto(comment);
    }

    // Crear un nuevo comentario
    public CommunityCommentDTO create(Long postId, CommunityCommentDTO commentDto) {
        if (postId == null) {
            throw new ValidationException("El ID del post no puede ser nulo");
        }
        if (commentDto == null) {
            throw new ValidationException("El comentario no puede ser nulo");
        }
        if (commentDto.getContent() == null || commentDto.getContent().trim().isEmpty()) {
            throw new ValidationException("El contenido del comentario es obligatorio");
        }
        
        CommunityPost post = communityPostRepository.findById(postId)
            .orElseThrow(() -> new ResourceNotFoundException("Post no encontrado con ID: " + postId));
        
        CommunityComment comment = new CommunityComment();
        comment.setPost(post);
        comment.setUserName(commentDto.getUserName());
        comment.setContent(commentDto.getContent());

        CommunityComment saved = communityCommentRepository.save(comment);
        return toDto(saved);
    }

    // Actualizar un comentario
    public CommunityCommentDTO update(Long id, CommunityCommentDTO updatedComment) {
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
        
        CommunityComment saved = communityCommentRepository.save(existing);
        return toDto(saved);
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

    private CommunityCommentDTO toDto(CommunityComment comment) {
        if (comment == null) {
            return null;
        }
        CommunityCommentDTO dto = new CommunityCommentDTO();
        dto.setId(comment.getId());
        dto.setPostId(comment.getPost() != null ? comment.getPost().getId() : null);
        dto.setUserName(comment.getUserName());
        dto.setContent(comment.getContent());
        dto.setCreatedAt(comment.getCreatedAt());
        return dto;
    }
}
