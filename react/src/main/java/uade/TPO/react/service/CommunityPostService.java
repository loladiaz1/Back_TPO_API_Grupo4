package uade.TPO.react.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import uade.TPO.react.dto.CommunityCommentDTO;
import uade.TPO.react.dto.CommunityPostDTO;
import uade.TPO.react.exception.ResourceNotFoundException;
import uade.TPO.react.exception.ValidationException;
import uade.TPO.react.repository.CommunityPostRepository;
import uade.TPO.react.entity.CommunityComment;
import uade.TPO.react.entity.CommunityPost;

@Service
public class CommunityPostService {

    @Autowired
    private CommunityPostRepository communityPostRepository;

    // Obtener todos los posts
    public List<CommunityPostDTO> getAll() {
        return communityPostRepository.findAll().stream()
            .map(this::toDto)
            .collect(Collectors.toList());
    }

    // Obtener posts ordenados por fecha (más recientes primero)
    public List<CommunityPostDTO> getAllOrderedByDate() {
        return communityPostRepository.findAllByOrderByCreatedAtDesc().stream()
            .map(this::toDto)
            .collect(Collectors.toList());
    }

    // Obtener un post por ID
    public CommunityPostDTO getById(Long id) {
        if (id == null) {
            throw new ValidationException("El ID no puede ser nulo");
        }
        CommunityPost post = communityPostRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Post no encontrado con ID: " + id));
        return toDto(post);
    }

    // Obtener posts por categoría
    public List<CommunityPostDTO> getByCategory(String category) {
        if (category == null || category.trim().isEmpty()) {
            throw new ValidationException("La categoría no puede ser nula o vacía");
        }
        return communityPostRepository.findByCategory(category).stream()
            .map(this::toDto)
            .collect(Collectors.toList());
    }

    // Obtener posts por autor
    public List<CommunityPostDTO> getByAuthor(String authorName) {
        if (authorName == null || authorName.trim().isEmpty()) {
            throw new ValidationException("El nombre del autor no puede ser nulo o vacío");
        }
        return communityPostRepository.findByAuthorName(authorName).stream()
            .map(this::toDto)
            .collect(Collectors.toList());
    }

    // Crear un nuevo post
    public CommunityPostDTO create(CommunityPostDTO postDto) {
        if (postDto == null) {
            throw new ValidationException("El post no puede ser nulo");
        }
        if (postDto.getTitle() == null || postDto.getTitle().trim().isEmpty()) {
            throw new ValidationException("El título del post es obligatorio");
        }
        if (postDto.getContent() == null || postDto.getContent().trim().isEmpty()) {
            throw new ValidationException("El contenido del post es obligatorio");
        }

        CommunityPost post = new CommunityPost();
        post.setAuthorName(postDto.getAuthorName());
        post.setTitle(postDto.getTitle());
        post.setContent(postDto.getContent());
        post.setCategory(postDto.getCategory());
        post.setImageUrl(postDto.getImageUrl());
        post.setLikes(postDto.getLikes() != null ? postDto.getLikes() : 0);

        CommunityPost saved = communityPostRepository.save(post);
        return toDto(saved);
    }

    // Actualizar un post
    public CommunityPostDTO update(Long id, CommunityPostDTO updatedPost) {
        if (id == null) {
            throw new ValidationException("El ID no puede ser nulo");
        }
        if (updatedPost == null) {
            throw new ValidationException("El post no puede ser nulo");
        }
        
        CommunityPost existing = communityPostRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Post no encontrado con ID: " + id));
        
        if (updatedPost.getTitle() != null) {
            existing.setTitle(updatedPost.getTitle());
        }
        if (updatedPost.getContent() != null) {
            existing.setContent(updatedPost.getContent());
        }
        if (updatedPost.getCategory() != null) {
            existing.setCategory(updatedPost.getCategory());
        }
        if (updatedPost.getImageUrl() != null) {
            existing.setImageUrl(updatedPost.getImageUrl());
        }
        if (updatedPost.getLikes() != null) {
            existing.setLikes(updatedPost.getLikes());
        }
        
        CommunityPost saved = communityPostRepository.save(existing);
        return toDto(saved);
    }

    // Eliminar un post
    public void delete(Long id) {
        if (id == null) {
            throw new ValidationException("El ID no puede ser nulo");
        }
        if (!communityPostRepository.existsById(id)) {
            throw new ResourceNotFoundException("Post no encontrado con ID: " + id);
        }
        communityPostRepository.deleteById(id);
    }

    // Incrementar likes
    public CommunityPostDTO addLike(Long id) {
        if (id == null) {
            throw new ValidationException("El ID no puede ser nulo");
        }
        CommunityPost post = communityPostRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Post no encontrado con ID: " + id));
        
        post.setLikes(post.getLikes() + 1);
        CommunityPost saved = communityPostRepository.save(post);
        return toDto(saved);
    }

    // Decrementar likes
    public CommunityPostDTO removeLike(Long id) {
        if (id == null) {
            throw new ValidationException("El ID no puede ser nulo");
        }
        CommunityPost post = communityPostRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Post no encontrado con ID: " + id));
        
        if (post.getLikes() > 0) {
            post.setLikes(post.getLikes() - 1);
        }
        
        CommunityPost saved = communityPostRepository.save(post);
        return toDto(saved);
    }

    // Obtener posts con filtro opcional por categoría
    public List<CommunityPostDTO> getAllPosts(String category) {
        if (category != null && !category.isEmpty()) {
            return getByCategory(category);
        }
        return getAllOrderedByDate();
    }

    private CommunityPostDTO toDto(CommunityPost post) {
        if (post == null) {
            return null;
        }
        CommunityPostDTO dto = new CommunityPostDTO();
        dto.setId(post.getId());
        dto.setAuthorName(post.getAuthorName());
        dto.setTitle(post.getTitle());
        dto.setContent(post.getContent());
        dto.setCategory(post.getCategory());
        dto.setImageUrl(post.getImageUrl());
        dto.setCreatedAt(post.getCreatedAt());
        dto.setLikes(post.getLikes());

        if (post.getComments() != null) {
            dto.setComments(post.getComments().stream()
                .map(this::toCommentDto)
                .collect(Collectors.toList()));
        }
        return dto;
    }

    private CommunityCommentDTO toCommentDto(CommunityComment comment) {
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
