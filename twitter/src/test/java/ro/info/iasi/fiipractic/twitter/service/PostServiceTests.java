package ro.info.iasi.fiipractic.twitter.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import ro.info.iasi.fiipractic.twitter.dto.response.PostLikesResponseDto;
import ro.info.iasi.fiipractic.twitter.dto.response.PostResponseDto;
import ro.info.iasi.fiipractic.twitter.exception.NotFoundException;
import ro.info.iasi.fiipractic.twitter.exception.UnauthorizedException;
import ro.info.iasi.fiipractic.twitter.model.Like;
import ro.info.iasi.fiipractic.twitter.model.Post;
import ro.info.iasi.fiipractic.twitter.model.User;
import ro.info.iasi.fiipractic.twitter.repository.PostJpaRepository;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class PostServiceTests {

    @InjectMocks
    private PostService postService;
    @Mock
    private PostJpaRepository postRepository;
    @Mock
    private FollowService followService;
    @Mock
    private LikeService likeService;
    private User user;

    @BeforeEach
    public void setUp() {
        postService = new PostService(postRepository, followService, likeService);
        user = new User("carl",
                "Carl",
                "Johan",
                "carl.johan@gmail.com",
                "password123");
    }

    @Test
    public void testSavePost() {
        Post post = new Post(user, "Hello, world!", System.currentTimeMillis());
        when(postRepository.save(post)).thenReturn(post);
        Post savedPost = postService.savePost(post);
        assertEquals(post, savedPost);
    }

    @Test
    public void testGetPostsByUser() {
        Post post1 = new Post(user, "Hello, world!", System.currentTimeMillis());
        Post post2 = new Post(user, "Goodbye, world!", System.currentTimeMillis());
        List<Post> posts = List.of(post1, post2);
        when(postRepository.getPostsByUser(user)).thenReturn(posts);
        List<Post> retrievedPosts = postService.getPostsByUser(user);
        assertEquals(posts, retrievedPosts);
    }

    @Test
    public void testGetPostsByUserWithTimeFilter() {
        long timestamp = System.currentTimeMillis();
        Post post1 = new Post(user, "Hello, world!", timestamp);
        Post post2 = new Post(user, "Goodbye, world!", timestamp);
        List<Post> posts = List.of(post1, post2);
        when(postRepository.getPostsByUserWithTimeFilter(user, timestamp)).thenReturn(posts);
        List<Post> retrievedPosts = postService.getPostsByUserWithTimeFilter(user, timestamp);
        assertEquals(posts, retrievedPosts);
    }

    @Test
    public void testGetFeed() {
        User followedUser1 = new User("user1", "User 1", "Lastname 1", "user1@gmail.com", "password123");
        User followedUser2 = new User("user2", "User 2", "Lastname 2", "user2@gmail.com", "password123");
        Post post1 = new Post(user, "Hello, world!", System.currentTimeMillis());
        Post post2 = new Post(user, "Goodbye, world!", System.currentTimeMillis());
        List<Post> posts = List.of(post1, post2);
        when(followService.getFollowedUsers(user)).thenReturn(List.of(followedUser1, followedUser2));
        when(postRepository.getPostsByUser(followedUser1)).thenReturn(List.of(post1));
        when(postRepository.getPostsByUser(followedUser2)).thenReturn(List.of(post2));
        List<Post> retrievedPosts = postService.getFeed(user);
        assertEquals(posts, retrievedPosts);
    }

    @Test
    public void testGetPostById() {
        UUID postId = UUID.randomUUID();
        Post post = new Post(user, "Hello, world!", System.currentTimeMillis());
        when(postRepository.getPostById(postId)).thenReturn(post);
        Post retrievedPost = postService.getPostById(postId);
        assertEquals(post, retrievedPost);
    }

    @Test
    public void testGetPostByIdNotFound() {
        UUID postId = UUID.randomUUID();
        when(postRepository.getPostById(postId)).thenReturn(null);
        assertThrows(NotFoundException.class, () -> postService.getPostById(postId));
    }

    @Test
    public void testDeletePost() {
        UUID postUserId = UUID.randomUUID();
        user = mock(User.class);
        Post post = mock(Post.class);
        post.setUser(user);
        when(user.getId()).thenReturn(postUserId);
        when(post.getUser()).thenReturn(user);
        doNothing().when(postRepository).delete(post);
        postService.deletePost(user, post);
        verify(postRepository, times(1)).delete(post);
    }

    @Test
    public void testDeletePostUnauthorizedUser() {
        UUID postId = UUID.randomUUID();
        user = mock(User.class);
        User user2 = mock(User.class);
        Post post = mock(Post.class);
        post.setUser(user);
        when(user2.getId()).thenReturn(UUID.randomUUID());
        when(user.getId()).thenReturn(postId);
        when(post.getUser()).thenReturn(user);
        assertThrows(UnauthorizedException.class, () -> postService.deletePost(user2, post));
    }

    @Test
    public void testGetPostsWithLikes() {
        Post post1 = new Post(user, "Hello, world!", System.currentTimeMillis());
        Post post2 = new Post(user, "Goodbye, world!", System.currentTimeMillis());
        List<Post> posts = List.of(post1, post2);
        when(likeService.getLikesByPost(post1)).thenReturn(List.of(new Like(user, post1)));
        when(likeService.getLikesByPost(post2)).thenReturn(List.of(new Like(user, post2)));
        List<PostLikesResponseDto> expected = List.of(
                new PostLikesResponseDto(post1.getId(), user.getUsername(), post1.getMessage(), post1.getTimestamp()),
                new PostLikesResponseDto(post2.getId(), user.getUsername(), post2.getMessage(), post2.getTimestamp())
        );
        expected.get(0).setLikes(List.of(user.getUsername()));
        expected.get(1).setLikes(List.of(user.getUsername()));
        List<PostLikesResponseDto> actual = postService.getPostsWithLikes(posts);
        assertEquals(expected, actual);
    }

    @Test
    public void getPostResponseDtosTest(){
        Post post1 = new Post(user, "Hello, world!", System.currentTimeMillis());
        Post post2 = new Post(user, "Goodbye, world!", System.currentTimeMillis());
        List<Post> posts = List.of(post1, post2);
        List<PostResponseDto> expected = List.of(
                new PostResponseDto(post1.getId(), user.getUsername(), post1.getMessage(), post1.getTimestamp()),
                new PostResponseDto(post2.getId(), user.getUsername(), post2.getMessage(), post2.getTimestamp())
        );
        List<PostResponseDto> actual = postService.getPostResponseDtos(posts);
        assertEquals(expected, actual);
    }

}

