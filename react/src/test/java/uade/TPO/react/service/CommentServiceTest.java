package uade.TPO.react.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import uade.TPO.react.entity.Comment;
import uade.TPO.react.entity.Game;
import uade.TPO.react.repository.CommentRepository;
import uade.TPO.react.repository.GameRepository;

@ExtendWith(MockitoExtension.class)
public class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private GameRepository gameRepository;

    @InjectMocks
    private CommentService commentService;

    private Game game;
    private Comment comment1;
    private Comment comment2;

    @BeforeEach
    void setUp() {
        game = new Game();
        game.setId(10L);
        game.setName("Test Game");

        comment1 = new Comment(game, "user1", "content1", 4);
        comment1.setId(1L);
        comment2 = new Comment(game, "user2", "content2", 2);
        comment2.setId(2L);
    }

    @Test
    void testGetAll() {
        when(commentRepository.findAll()).thenReturn(Arrays.asList(comment1, comment2));
        List<Comment> result = commentService.getAll();
        assertEquals(2, result.size());
        verify(commentRepository).findAll();
    }

    @Test
    void testGetByGameId() {
        when(commentRepository.findByGameId(10L)).thenReturn(Arrays.asList(comment1));
        List<Comment> result = commentService.getByGameId(10L);
        assertEquals(1, result.size());
        assertEquals("user1", result.get(0).getUserName());
    }

    @Test
    void testGetByIdFound() {
        when(commentRepository.findById(1L)).thenReturn(Optional.of(comment1));
        Comment res = commentService.getById(1L);
        assertNotNull(res);
        assertEquals(1L, res.getId());
    }

    @Test
    void testGetByIdNotFound() {
        when(commentRepository.findById(99L)).thenReturn(Optional.empty());
        assertNull(commentService.getById(99L));
    }

    @Test
    void testCreateSuccess() {
        when(gameRepository.findById(10L)).thenReturn(Optional.of(game));
        ArgumentCaptor<Comment> captor = ArgumentCaptor.forClass(Comment.class);
        when(commentRepository.save(any(Comment.class))).thenAnswer(inv -> {
            Comment c = inv.getArgument(0);
            c.setId(5L);
            return c;
        });

        Comment toCreate = new Comment();
        toCreate.setContent("nuevo");
        toCreate.setRating(5);
        toCreate.setUserName("u");

        Comment created = commentService.create(10L, toCreate);
        verify(gameRepository).findById(10L);
        verify(commentRepository).save(captor.capture());
        assertEquals(5L, created.getId());
        assertEquals(game, captor.getValue().getGame());
    }

    @Test
    void testCreateGameNotFound() {
        when(gameRepository.findById(999L)).thenReturn(Optional.empty());
        Comment c = new Comment();
        c.setRating(3);
        RuntimeException ex = assertThrows(RuntimeException.class, () -> commentService.create(999L, c));
        assertTrue(ex.getMessage().contains("Juego no encontrado"));
    }

    @Test
    void testCreateInvalidRating() {
        when(gameRepository.findById(10L)).thenReturn(Optional.of(game));
        Comment c = new Comment();
        c.setRating(0);
        RuntimeException ex = assertThrows(RuntimeException.class, () -> commentService.create(10L, c));
        assertTrue(ex.getMessage().contains("rating"));
    }

    @Test
    void testUpdateSuccess() {
        when(commentRepository.findById(1L)).thenReturn(Optional.of(comment1));
        when(commentRepository.save(any(Comment.class))).thenAnswer(i -> i.getArgument(0));

        Comment updated = new Comment();
        updated.setContent("modificado");
        updated.setRating(5);

        Comment res = commentService.update(1L, updated);
        assertEquals("modificado", res.getContent());
        assertEquals(5, res.getRating().intValue());
        verify(commentRepository).save(any(Comment.class));
    }

    @Test
    void testUpdateNotFound() {
        when(commentRepository.findById(50L)).thenReturn(Optional.empty());
        Comment updated = new Comment();
        RuntimeException ex = assertThrows(RuntimeException.class, () -> commentService.update(50L, updated));
        assertTrue(ex.getMessage().contains("Comentario no encontrado"));
    }

    @Test
    void testUpdateInvalidRating() {
        when(commentRepository.findById(1L)).thenReturn(Optional.of(comment1));
        Comment updated = new Comment();
        updated.setRating(99);
        RuntimeException ex = assertThrows(RuntimeException.class, () -> commentService.update(1L, updated));
        assertTrue(ex.getMessage().contains("rating"));
    }

    @Test
    void testDelete() {
        doNothing().when(commentRepository).deleteById(1L);
        commentService.delete(1L);
        verify(commentRepository).deleteById(1L);
    }

    @Test
    void testGetAverageRatingEmpty() {
        when(commentRepository.findByGameId(10L)).thenReturn(Collections.emptyList());
        Double avg = commentService.getAverageRating(10L);
        assertEquals(0.0, avg);
    }

    @Test
    void testGetAverageRatingNonEmpty() {
        when(commentRepository.findByGameId(10L)).thenReturn(Arrays.asList(comment1, comment2));
        Double avg = commentService.getAverageRating(10L);
        assertEquals((4 + 2) / 2.0, avg);
    }

    @Test
    void testCountByGameIdAndStats() {
        when(commentRepository.countByGameId(10L)).thenReturn(2L);
        when(commentRepository.findByGameId(10L)).thenReturn(Arrays.asList(comment1, comment2));

        long count = commentService.countByGameId(10L);
        assertEquals(2L, count);

        Map<String, Object> stats = commentService.getGameCommentStats(10L);
        assertEquals(2L, stats.get("totalComments"));
        assertEquals((4 + 2) / 2.0, (Double) stats.get("averageRating"));
    }
}
