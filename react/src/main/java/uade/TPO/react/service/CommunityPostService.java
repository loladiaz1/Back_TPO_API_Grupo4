package uade.TPO.react.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import uade.TPO.react.entity.CommunityPost;
import uade.TPO.react.exception.ResourceNotFoundException;
import uade.TPO.react.exception.ValidationException;
import uade.TPO.react.repository.CommunityPostRepository;

@Service
public class CommunityPostService {

    @Autowired
    private CommunityPostRepository communityPostRepository;

    // Obtener todos los posts
    public List<CommunityPost> getAll() {
        return communityPostRepository.findAll();
    }

    // Obtener posts ordenados por fecha (más recientes primero)
    public List<CommunityPost> getAllOrderedByDate() {
        return communityPostRepository.findAllByOrderByCreatedAtDesc();
    }

    // Obtener un post por ID
    public CommunityPost getById(Long id) {
        if (id == null) {
            throw new ValidationException("El ID no puede ser nulo");
        }
        return communityPostRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Post no encontrado con ID: " + id));
    }

    // Obtener posts por categoría
    public List<CommunityPost> getByCategory(String category) {
        if (category == null || category.trim().isEmpty()) {
            throw new ValidationException("La categoría no puede ser nula o vacía");
        }
        return communityPostRepository.findByCategory(category);
    }

    // Obtener posts por autor
    public List<CommunityPost> getByAuthor(String authorName) {
        if (authorName == null || authorName.trim().isEmpty()) {
            throw new ValidationException("El nombre del autor no puede ser nulo o vacío");
        }
        return communityPostRepository.findByAuthorName(authorName);
    }

    // Crear un nuevo post
    public CommunityPost create(CommunityPost post) {
        if (post == null) {
            throw new ValidationException("El post no puede ser nulo");
        }
        if (post.getTitle() == null || post.getTitle().trim().isEmpty()) {
            throw new ValidationException("El título del post es obligatorio");
        }
        if (post.getContent() == null || post.getContent().trim().isEmpty()) {
            throw new ValidationException("El contenido del post es obligatorio");
        }
        return communityPostRepository.save(post);
    }

    // Actualizar un post
    public CommunityPost update(Long id, CommunityPost updatedPost) {
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
        
        return communityPostRepository.save(existing);
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
    public CommunityPost addLike(Long id) {
        if (id == null) {
            throw new ValidationException("El ID no puede ser nulo");
        }
        CommunityPost post = communityPostRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Post no encontrado con ID: " + id));
        
        post.setLikes(post.getLikes() + 1);
        return communityPostRepository.save(post);
    }

    // Decrementar likes
    public CommunityPost removeLike(Long id) {
        if (id == null) {
            throw new ValidationException("El ID no puede ser nulo");
        }
        CommunityPost post = communityPostRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Post no encontrado con ID: " + id));
        
        if (post.getLikes() > 0) {
            post.setLikes(post.getLikes() - 1);
        }
        
        return communityPostRepository.save(post);
    }

    // Obtener posts con filtro opcional por categoría
    public List<CommunityPost> getAllPosts(String category) {
        if (category != null && !category.isEmpty()) {
            return getByCategory(category);
        }
        return getAllOrderedByDate();
    }
}
