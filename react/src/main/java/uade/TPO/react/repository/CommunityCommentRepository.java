package uade.TPO.react.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import uade.TPO.react.entity.CommunityComment;

@Repository
public interface CommunityCommentRepository extends JpaRepository<CommunityComment, Long> {
    
    // Encontrar todos los comentarios de un post específico
    List<CommunityComment> findByPostId(Long postId);
    
    // Encontrar comentarios por nombre de usuario
    List<CommunityComment> findByUserName(String userName);
}
