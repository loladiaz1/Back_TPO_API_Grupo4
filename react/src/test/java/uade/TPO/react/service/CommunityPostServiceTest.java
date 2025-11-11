package uade.TPO.react.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
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

import uade.TPO.react.dto.CommunityPostDTO;
import uade.TPO.react.entity.CommunityPost;
import uade.TPO.react.exception.ResourceNotFoundException;
import uade.TPO.react.repository.CommunityPostRepository;

@ExtendWith(MockitoExtension.class)
public class CommunityPostServiceTest {

    @Mock
    private CommunityPostRepository communityPostRepository;

    @InjectMocks
    private CommunityPostService communityPostService;

    private CommunityPost post1;
    private CommunityPost post2;

    @BeforeEach
    void setUp() {
        post1 = new CommunityPost("author1", "title1", "content1", "Discusión");
        post1.setId(1L);
        post1.setCreatedAt(LocalDateTime.now().minusDays(1));
        post1.setLikes(2);

        post2 = new CommunityPost("author2", "title2", "content2", "Noticia");
        post2.setId(2L);
        post2.setCreatedAt(LocalDateTime.now());
        post2.setLikes(0);
    }

    @Test
    void testGetAll() {
        when(communityPostRepository.findAll()).thenReturn(Arrays.asList(post1, post2));
        List<CommunityPostDTO> res = communityPostService.getAll();
        assertEquals(2, res.size());
        verify(communityPostRepository).findAll();
    }

    @Test
    void testGetAllOrderedByDate() {
        when(communityPostRepository.findAllByOrderByCreatedAtDesc()).thenReturn(Arrays.asList(post2, post1));
        List<CommunityPostDTO> res = communityPostService.getAllOrderedByDate();
        assertEquals(2, res.size());
        assertEquals(post2.getId(), res.get(0).getId());
    }

    @Test
    void testGetByIdFound() {
        when(communityPostRepository.findById(1L)).thenReturn(Optional.of(post1));
        CommunityPostDTO res = communityPostService.getById(1L);
        assertNotNull(res);
        assertEquals(1L, res.getId());
    }

    @Test
    void testGetByIdNotFound() {
        when(communityPostRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> communityPostService.getById(99L));
    }

    @Test
    void testGetByCategory() {
        when(communityPostRepository.findByCategory("Discusión")).thenReturn(Arrays.asList(post1));
        List<CommunityPostDTO> res = communityPostService.getByCategory("Discusión");
        assertEquals(1, res.size());
        assertEquals("Discusión", res.get(0).getCategory());
    }

    @Test
    void testGetByAuthor() {
        when(communityPostRepository.findByAuthorName("author2")).thenReturn(Arrays.asList(post2));
        List<CommunityPostDTO> res = communityPostService.getByAuthor("author2");
        assertEquals(1, res.size());
        assertEquals("author2", res.get(0).getAuthorName());
    }

    @Test
    void testCreate() {
        ArgumentCaptor<CommunityPost> captor = ArgumentCaptor.forClass(CommunityPost.class);
        when(communityPostRepository.save(any(CommunityPost.class))).thenAnswer(inv -> {
            CommunityPost p = inv.getArgument(0);
            p.setId(33L);
            return p;
        });

        CommunityPostDTO toCreate = new CommunityPostDTO();
        toCreate.setAuthorName("a");
        toCreate.setTitle("t");
        toCreate.setContent("c");
        toCreate.setCategory("Cat");

        CommunityPostDTO created = communityPostService.create(toCreate);
        verify(communityPostRepository).save(captor.capture());
        assertEquals(33L, created.getId());
        assertEquals("t", captor.getValue().getTitle());
    }

    @Test
    void testUpdateSuccess() {
        when(communityPostRepository.findById(1L)).thenReturn(Optional.of(post1));
        when(communityPostRepository.save(any(CommunityPost.class))).thenAnswer(i -> i.getArgument(0));

        CommunityPostDTO updated = new CommunityPostDTO();
        updated.setTitle("new title");
        updated.setContent("new content");

        CommunityPostDTO res = communityPostService.update(1L, updated);
        assertEquals("new title", res.getTitle());
        assertEquals("new content", res.getContent());
        verify(communityPostRepository).save(any(CommunityPost.class));
    }

    @Test
    void testUpdateNotFound() {
        when(communityPostRepository.findById(50L)).thenReturn(Optional.empty());
        CommunityPostDTO updated = new CommunityPostDTO();
        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () -> communityPostService.update(50L, updated));
        assertTrue(ex.getMessage().contains("Post no encontrado"));
    }

    @Test
    void testDelete() {
        doNothing().when(communityPostRepository).deleteById(1L);
        communityPostService.delete(1L);
        verify(communityPostRepository).deleteById(1L);
    }

    @Test
    void testAddLikeSuccess() {
        when(communityPostRepository.findById(1L)).thenReturn(Optional.of(post1));
        when(communityPostRepository.save(any(CommunityPost.class))).thenAnswer(i -> i.getArgument(0));

        CommunityPostDTO res = communityPostService.addLike(1L);
        assertEquals(3, res.getLikes().intValue());
        verify(communityPostRepository).save(any(CommunityPost.class));
    }

    @Test
    void testAddLikeNotFound() {
        when(communityPostRepository.findById(99L)).thenReturn(Optional.empty());
        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () -> communityPostService.addLike(99L));
        assertTrue(ex.getMessage().contains("Post no encontrado"));
    }

    @Test
    void testRemoveLikeSuccess() {
        when(communityPostRepository.findById(1L)).thenReturn(Optional.of(post1));
        when(communityPostRepository.save(any(CommunityPost.class))).thenAnswer(i -> i.getArgument(0));

        CommunityPostDTO res = communityPostService.removeLike(1L);
        assertEquals(1, res.getLikes().intValue());
        verify(communityPostRepository).save(any(CommunityPost.class));
    }

    @Test
    void testRemoveLikeNotFound() {
        when(communityPostRepository.findById(99L)).thenReturn(Optional.empty());
        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () -> communityPostService.removeLike(99L));
        assertTrue(ex.getMessage().contains("Post no encontrado"));
    }

    @Test
    void testRemoveLikeWhenZero() {
        when(communityPostRepository.findById(2L)).thenReturn(Optional.of(post2));
        when(communityPostRepository.save(any(CommunityPost.class))).thenAnswer(i -> i.getArgument(0));

        CommunityPostDTO res = communityPostService.removeLike(2L);
        assertEquals(0, res.getLikes().intValue());
        verify(communityPostRepository).save(any(CommunityPost.class));
    }

    @Test
    void testGetAllPostsWithCategory() {
        when(communityPostRepository.findByCategory("Noticia")).thenReturn(Arrays.asList(post2));
        List<CommunityPostDTO> res = communityPostService.getAllPosts("Noticia");
        assertEquals(1, res.size());
        assertEquals("Noticia", res.get(0).getCategory());
    }

    @Test
    void testGetAllPostsNoCategory() {
        when(communityPostRepository.findAllByOrderByCreatedAtDesc()).thenReturn(Arrays.asList(post2, post1));
        List<CommunityPostDTO> res = communityPostService.getAllPosts("");
        assertEquals(2, res.size());
        assertEquals(post2.getId(), res.get(0).getId());
    }
}

