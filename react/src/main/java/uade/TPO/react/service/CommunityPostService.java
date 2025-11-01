package uade.TPO.react.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import uade.TPO.react.entity.CommunityPost;
import uade.TPO.react.exception.ResourceNotFoundException;
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
        return communityPostRepository.findById(id).orElse(null);
    }

    // Obtener posts por categoría
    public List<CommunityPost> getByCategory(String category) {
        return communityPostRepository.findByCategory(category);
    }

    // Obtener posts por autor
    public List<CommunityPost> getByAuthor(String authorName) {
        return communityPostRepository.findByAuthorName(authorName);
    }

    // Crear un nuevo post
    public CommunityPost create(CommunityPost post) {
        return communityPostRepository.save(post);
    }

    // Actualizar un post
    public CommunityPost update(Long id, CommunityPost updatedPost) {
        CommunityPost existing = communityPostRepository.findById(id).orElse(null);
        if (existing == null) {
            throw new ResourceNotFoundException("Post no encontrado con ID: " + id);
        }
        
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
        communityPostRepository.deleteById(id);
    }

    // Incrementar likes
    public CommunityPost addLike(Long id) {
        CommunityPost post = communityPostRepository.findById(id).orElse(null);
        if (post == null) {
            throw new ResourceNotFoundException("Post no encontrado con ID: " + id);
        }
        
        post.setLikes(post.getLikes() + 1);
        return communityPostRepository.save(post);
    }

    // Decrementar likes
    public CommunityPost removeLike(Long id) {
        CommunityPost post = communityPostRepository.findById(id).orElse(null);
        if (post == null) {
            throw new ResourceNotFoundException("Post no encontrado con ID: " + id);
        }
        
        if (post.getLikes() > 0) {
            post.setLikes(post.getLikes() - 1);
        }
        
        return communityPostRepository.save(post);
    }
}
