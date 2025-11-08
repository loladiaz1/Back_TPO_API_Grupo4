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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import uade.TPO.react.entity.CommunityPost;
import uade.TPO.react.service.CommunityPostService;

@RestController
@RequestMapping("/community/posts")
//@CrossOrigin(origins = "*")
public class CommunityPostController {

    @Autowired
    private CommunityPostService communityPostService;

    // Obtener todos los posts (ordenados por fecha)
    @GetMapping
    public List<CommunityPost> getAllPosts(@RequestParam(required = false) String category) {
        if (category != null && !category.isEmpty()) {
            return communityPostService.getByCategory(category);
        }
        return communityPostService.getAllOrderedByDate();
    }

    // Obtener un post por ID
    @GetMapping("/{id}")
    public CommunityPost getPostById(@PathVariable Long id) {
        return communityPostService.getById(id);
    }

    // Obtener posts por autor
    @GetMapping("/author/{authorName}")
    public List<CommunityPost> getPostsByAuthor(@PathVariable String authorName) {
        return communityPostService.getByAuthor(authorName);
    }

    // Crear un nuevo post
    @PostMapping
    public CommunityPost createPost(@RequestBody CommunityPost post) {
        return communityPostService.create(post);
    }

    // Actualizar un post
    @PutMapping("/{id}")
    public CommunityPost updatePost(@PathVariable Long id, @RequestBody CommunityPost post) {
        return communityPostService.update(id, post);
    }

    // Eliminar un post
    @DeleteMapping("/{id}")
    public void deletePost(@PathVariable Long id) {
        communityPostService.delete(id);
    }

    // Agregar like a un post
    @PostMapping("/{id}/like")
    public CommunityPost addLike(@PathVariable Long id) {
        return communityPostService.addLike(id);
    }

    // Quitar like de un post
    @PostMapping("/{id}/unlike")
    public CommunityPost removeLike(@PathVariable Long id) {
        return communityPostService.removeLike(id);
    }
}
