package uade.TPO.react.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import uade.TPO.react.entity.CommunityPost;
import uade.TPO.react.service.CommunityPostService;

@RestController
@RequestMapping("/api/community-posts")
@CrossOrigin(origins = "*")
public class CommunityPostController {

    @Autowired
    private CommunityPostService communityPostService;

    // Obtener todos los posts (ordenados por fecha)
    @GetMapping
    public List<CommunityPost> getAllPosts(@RequestParam(required = false) String category) {
        return communityPostService.getAllPosts(category);
    }

    // Obtener un post por ID
    @GetMapping("/{id}")
    public ResponseEntity<CommunityPost> getPostById(@PathVariable Long id) {
        CommunityPost post = communityPostService.getById(id);
        if (post == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(post);
    }

    // Obtener posts por autor
    @GetMapping("/author/{authorName}")
    public List<CommunityPost> getPostsByAuthor(@PathVariable String authorName) {
        return communityPostService.getByAuthor(authorName);
    }

    // Crear un nuevo post
    @PostMapping
    public ResponseEntity<CommunityPost> createPost(@RequestBody CommunityPost post) {
        CommunityPost created = communityPostService.create(post);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // Actualizar un post
    @PutMapping("/{id}")
    public ResponseEntity<?> updatePost(@PathVariable Long id, @RequestBody CommunityPost post) {
        try {
            CommunityPost updated = communityPostService.update(id, post);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // Eliminar un post
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        communityPostService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // Agregar like a un post
    @PostMapping("/{id}/like")
    public ResponseEntity<?> addLike(@PathVariable Long id) {
        try {
            CommunityPost updated = communityPostService.addLike(id);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // Quitar like de un post
    @PostMapping("/{id}/unlike")
    public ResponseEntity<?> removeLike(@PathVariable Long id) {
        try {
            CommunityPost updated = communityPostService.removeLike(id);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
