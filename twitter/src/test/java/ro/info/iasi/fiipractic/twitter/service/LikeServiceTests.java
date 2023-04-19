package ro.info.iasi.fiipractic.twitter.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import ro.info.iasi.fiipractic.twitter.exception.BadRequestException;
import ro.info.iasi.fiipractic.twitter.exception.NotFoundException;
import ro.info.iasi.fiipractic.twitter.model.Like;
import ro.info.iasi.fiipractic.twitter.model.Post;
import ro.info.iasi.fiipractic.twitter.model.User;
import ro.info.iasi.fiipractic.twitter.repository.LikeJpaRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class LikeServiceTests {

    @InjectMocks
    private LikeService likeService;

    @Mock
    private LikeJpaRepository likeJpaRepository;
    @Mock
    private Like like;

    @BeforeEach
    void setUp() {
        likeService = new LikeService(likeJpaRepository);
    }

    @Test
    void testSaveLike() {
        when(like.getPost()).thenReturn(mock(Post.class));
        when(like.getUser()).thenReturn(mock(User.class));
        when(likeJpaRepository.save(like)).thenReturn(like);
        Like result = likeService.saveLike(like);
        assertEquals(like, result);
    }

    @Test
    void testSaveLikeAlreadyExists() {
        when(like.getPost()).thenReturn(mock(Post.class));
        when(like.getUser()).thenReturn(mock(User.class));
        when(likeJpaRepository.findLikeByUserAndPost(like.getUser(), like.getPost())).thenReturn(like);
        assertThrows(BadRequestException.class, () -> likeService.saveLike(like));
    }

    @Test
    void testGetLikeByUserAndPost() {
        when(like.getPost()).thenReturn(mock(Post.class));
        when(like.getUser()).thenReturn(mock(User.class));
        when(likeJpaRepository.findLikeByUserAndPost(like.getUser(), like.getPost())).thenReturn(like);
        Like result = likeService.getLikeByUserAndPost(like.getUser(), like.getPost());
        assertEquals(like, result);
    }


    @Test
    void testGetLikeByUserAndPostNotFound() {
        when(like.getPost()).thenReturn(mock(Post.class));
        when(like.getUser()).thenReturn(mock(User.class));
        when(likeJpaRepository.findLikeByUserAndPost(like.getUser(), like.getPost())).thenReturn(null);
        assertThrows(NotFoundException.class, () -> likeService.getLikeByUserAndPost(like.getUser(), like.getPost()));
    }


    @Test
    void testRemoveLike() {
        doNothing().when(likeJpaRepository).delete(like);
        likeService.removeLike(like);
        verify(likeJpaRepository, times(1)).delete(like);
    }


    @Test
    void testGetLikesByPost() {
        when(like.getPost()).thenReturn(mock(Post.class));
        List<Like> likes = List.of(like);
        when(likeJpaRepository.findLikesByPost(like.getPost())).thenReturn(likes);
        List<Like> result = likeService.getLikesByPost(like.getPost());
        assertEquals(likes, result);
    }
}
