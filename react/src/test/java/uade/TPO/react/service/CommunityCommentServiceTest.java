package uade.TPO.react.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import uade.TPO.react.dto.CommunityCommentDTO;
import uade.TPO.react.entity.CommunityComment;
import uade.TPO.react.entity.CommunityPost;
import uade.TPO.react.exception.ResourceNotFoundException;
import uade.TPO.react.repository.CommunityCommentRepository;
import uade.TPO.react.repository.CommunityPostRepository;

@ExtendWith(MockitoExtension.class)
public class CommunityCommentServiceTest {

    @Mock
    private CommunityCommentRepository communityCommentRepository;

    @Mock
    private CommunityPostRepository communityPostRepository;

    @InjectMocks
    private CommunityCommentService communityCommentService;

    private CommunityPost post;
    private CommunityComment comment1;
    private CommunityComment comment2;

    @BeforeEach
    void setUp() {
        post = new CommunityPost("author", "title", "content", "Discusión");
        post.setId(1L);

        comment1 = new CommunityComment(post, "user1", "coment1");
        comment1.setId(10L);
        comment2 = new CommunityComment(post, "user2", "coment2");
        comment2.setId(11L);
    }

    @Test
    void testGetAll() {
        when(communityCommentRepository.findAll()).thenReturn(Arrays.asList(comment1, comment2));
        List<CommunityCommentDTO> res = communityCommentService.getAll();
        assertEquals(2, res.size());
        verify(communityCommentRepository).findAll();
    }

    @Test
    void testGetByPostId() {
        when(communityCommentRepository.findByPostId(1L)).thenReturn(Arrays.asList(comment1));
        List<CommunityCommentDTO> res = communityCommentService.getByPostId(1L);
        assertEquals(1, res.size());
        assertEquals("user1", res.get(0).getUserName());
    }

    @Test
    void testGetByIdFound() {
        when(communityCommentRepository.findById(10L)).thenReturn(Optional.of(comment1));
        CommunityCommentDTO res = communityCommentService.getById(10L);
        assertNotNull(res);
        assertEquals(10L, res.getId());
    }

    @Test
    void testGetByIdNotFound() {
        when(communityCommentRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> communityCommentService.getById(99L));
    }

    @Test
    void testCreateSuccess() {
        when(communityPostRepository.findById(1L)).thenReturn(Optional.of(post));
        ArgumentCaptor<CommunityComment> captor = ArgumentCaptor.forClass(CommunityComment.class);
        when(communityCommentRepository.save(any(CommunityComment.class))).thenAnswer(inv -> {
            CommunityComment c = inv.getArgument(0);
            c.setId(55L);
            return c;
        });

        CommunityCommentDTO toCreate = new CommunityCommentDTO();
        toCreate.setUserName("nuevo");
        toCreate.setContent("contenido");

        CommunityCommentDTO created = communityCommentService.create(1L, toCreate);
        verify(communityPostRepository).findById(1L);
        verify(communityCommentRepository).save(captor.capture());
        assertEquals(55L, created.getId());
        assertEquals(post, captor.getValue().getPost());
        assertEquals("nuevo", created.getUserName());
    }

    @Test
    void testCreatePostNotFound() {
        when(communityPostRepository.findById(999L)).thenReturn(Optional.empty());
        CommunityCommentDTO c = new CommunityCommentDTO();
        RuntimeException ex = assertThrows(RuntimeException.class, () -> communityCommentService.create(999L, c));
        assertTrue(ex.getMessage().contains("Post no encontrado"));
    }

    @Test
    void testUpdateSuccess() {
        when(communityCommentRepository.findById(10L)).thenReturn(Optional.of(comment1));
        when(communityCommentRepository.save(any(CommunityComment.class))).thenAnswer(i -> i.getArgument(0));

        CommunityCommentDTO updated = new CommunityCommentDTO();
        updated.setContent("modificado");

        CommunityCommentDTO res = communityCommentService.update(10L, updated);
        assertEquals("modificado", res.getContent());
        verify(communityCommentRepository).save(any(CommunityComment.class));
    }

    @Test
    void testUpdateNotFound() {
        when(communityCommentRepository.findById(50L)).thenReturn(Optional.empty());
        CommunityCommentDTO updated = new CommunityCommentDTO();
        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () -> communityCommentService.update(50L, updated));
        assertTrue(ex.getMessage().contains("Comentario no encontrado"));
    }

    @Test
    void testDelete() {
        doNothing().when(communityCommentRepository).deleteById(10L);
        communityCommentService.delete(10L);
        verify(communityCommentRepository).deleteById(10L);
    }
}

