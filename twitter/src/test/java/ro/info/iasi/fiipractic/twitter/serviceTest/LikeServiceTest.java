package ro.info.iasi.fiipractic.twitter.serviceTest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import ro.info.iasi.fiipractic.twitter.exception.BadRequestException;
import ro.info.iasi.fiipractic.twitter.exception.NotFoundException;
import ro.info.iasi.fiipractic.twitter.model.Like;
import ro.info.iasi.fiipractic.twitter.model.Post;
import ro.info.iasi.fiipractic.twitter.model.User;
import ro.info.iasi.fiipractic.twitter.repository.LikeJpaRepository;
import ro.info.iasi.fiipractic.twitter.service.LikeService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class LikeServiceTest {

    @InjectMocks
    private LikeService likeService;

    @Mock
    private LikeJpaRepository likeJpaRepository;
    @Mock
    private Like like;

    @Before
    public void setUp() {
        likeService = new LikeService(likeJpaRepository);
        User user = mock(User.class);
        Post post = mock(Post.class);
        when(like.getUser()).thenReturn(user);
        when(like.getPost()).thenReturn(post);
    }

    @Test
    public void testSaveLike() {
        when(likeJpaRepository.save(like)).thenReturn(like);
        Like result = likeService.saveLike(like);
        assertEquals(like, result);
    }

    @Test
    public void testSaveLikeAlreadyExists() {
        when(likeJpaRepository.findLikeByUserAndPost(like.getUser(), like.getPost())).thenReturn(like);
        assertThrows(BadRequestException.class, () -> likeService.saveLike(like));
    }

    @Test
    public void testGetLikeByUserAndPost() {
        when(likeJpaRepository.findLikeByUserAndPost(like.getUser(), like.getPost())).thenReturn(like);
        Like result = likeService.getLikeByUserAndPost(like.getUser(), like.getPost());
        assertEquals(like, result);
    }


    @Test
    public void testGetLikeByUserAndPostNotFound() {
        when(likeJpaRepository.findLikeByUserAndPost(like.getUser(), like.getPost())).thenReturn(null);
        assertThrows(NotFoundException.class, () -> likeService.getLikeByUserAndPost(like.getUser(), like.getPost()));
    }


    @Test
    public void testRemoveLike() {
        doNothing().when(likeJpaRepository).delete(like);
        likeService.removeLike(like);
        verify(likeJpaRepository, times(1)).delete(like);
    }


    @Test
    public void testGetLikesByPost() {
        List<Like> likes = List.of(like);
        when(likeJpaRepository.findLikesByPost(like.getPost())).thenReturn(likes);
        List<Like> result = likeService.getLikesByPost(like.getPost());
        assertEquals(likes, result);
    }
}
