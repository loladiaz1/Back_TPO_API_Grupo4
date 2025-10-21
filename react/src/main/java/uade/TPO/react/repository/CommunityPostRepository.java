package uade.TPO.react.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import uade.TPO.react.entity.CommunityPost;

@Repository
public interface CommunityPostRepository extends JpaRepository<CommunityPost, Long> {
    
    // Encontrar posts por categoría
    List<CommunityPost> findByCategory(String category);
    
    // Encontrar posts por autor
    List<CommunityPost> findByAuthorName(String authorName);
    
    // Encontrar posts ordenados por fecha (más recientes primero)
    List<CommunityPost> findAllByOrderByCreatedAtDesc();
}
