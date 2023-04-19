package ro.info.iasi.fiipractic.twitter.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import ro.info.iasi.fiipractic.twitter.dto.response.ReplyResponseDto;
import ro.info.iasi.fiipractic.twitter.model.Post;
import ro.info.iasi.fiipractic.twitter.model.Reply;
import ro.info.iasi.fiipractic.twitter.model.User;
import ro.info.iasi.fiipractic.twitter.repository.ReplyJpaRepository;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class ReplyServiceTests {
    @InjectMocks
    ReplyService replyService;

    @Mock
    ReplyJpaRepository jpaRepository;

    @Mock
    Reply reply;

    @BeforeEach
    void setUp() {
        replyService = new ReplyService(jpaRepository);
    }

    @Test
    void saveReplyTest() {
        when(jpaRepository.save(reply)).thenReturn(reply);
        Reply actual = replyService.saveReply(reply);
        assertEquals(reply, actual);
    }

    @Test
    void getRepliesByPostTest() {
        Post post = mock(Post.class);
        UUID parentPostId = UUID.randomUUID();
        List<Reply> replies = List.of(reply);
        when(post.getId()).thenReturn(parentPostId);
        when(jpaRepository.findRepliesByParentPostId(parentPostId)).thenReturn(replies);
        List<Reply> actual = replyService.getRepliesByPost(post);
        assertEquals(replies, actual);
    }

    @Test
    void testGetReplyResponseDtosAsUser() {
        // Arrange
        User author = mock(User.class);
        when(author.getId()).thenReturn(UUID.randomUUID());
        Post post = mock(Post.class);
        UUID postId = UUID.randomUUID();
        when(post.getId()).thenReturn(postId);
        User user = mock(User.class);
        when(user.getId()).thenReturn(UUID.randomUUID());
        when(user.getUsername()).thenReturn("user");
        User otherUser = mock(User.class);
        when(otherUser.getId()).thenReturn(UUID.randomUUID());
        when(post.getUser()).thenReturn(author);
        long timestamp = System.currentTimeMillis();
        Reply publicReply = new Reply(user, "Public reply", timestamp, post, true);
        Reply userReply = new Reply(user, "User reply", timestamp, post, false);
        Reply userReply2 = new Reply(user, "User reply 2", timestamp, post, false);
        Reply otherUserReply = new Reply(otherUser, "Other user reply", timestamp, post, false);
        List<Reply> replies = List.of(publicReply, userReply, userReply2, otherUserReply);
        List<ReplyResponseDto> expectedReplies = getExpectedRepliesAsUser(user, publicReply, userReply, userReply2);
        // Act
        List<ReplyResponseDto> actualReplies = replyService.getReplyResponseDtos(user, replies);

        // Assert
        assertEquals(expectedReplies, actualReplies);
    }

    @Test
    void testGetReplyResponseDtosAsAuthor() {
        // Arrange
        User author = mock(User.class);
        when(author.getId()).thenReturn(UUID.randomUUID());
        Post post = mock(Post.class);
        UUID postId = UUID.randomUUID();
        when(post.getId()).thenReturn(postId);
        User user = mock(User.class);
        when(user.getUsername()).thenReturn("user");
        User otherUser = mock(User.class);
        when(post.getUser()).thenReturn(author);
        long timestamp = System.currentTimeMillis();
        Reply publicReply = new Reply(user, "Public reply", timestamp, post, true);
        Reply userReply = new Reply(user, "User reply", timestamp, post, false);
        Reply userReply2 = new Reply(user, "User reply 2", timestamp, post, false);
        Reply otherUserReply = new Reply(otherUser, "Other user reply", timestamp, post, false);
        List<Reply> replies = List.of(publicReply, userReply, userReply2, otherUserReply);
        List<ReplyResponseDto> expectedReplies = getExpectedRepliesAsAuthor(user,
                otherUser,
                publicReply,
                userReply,
                userReply2,
                otherUserReply);
        // Act
        List<ReplyResponseDto> actualReplies = replyService.getReplyResponseDtos(author, replies);

        // Assert
        assertEquals(expectedReplies, actualReplies);
    }

    @Test
    void testGetReplyResponseDtosAsOtherUser() {
        // Arrange
        User author = mock(User.class);
        when(author.getId()).thenReturn(UUID.randomUUID());
        Post post = mock(Post.class);
        UUID postId = UUID.randomUUID();
        when(post.getId()).thenReturn(postId);
        User user = mock(User.class);
        when(user.getId()).thenReturn(UUID.randomUUID());
        when(user.getUsername()).thenReturn("user");
        User otherUser = mock(User.class);
        when(otherUser.getId()).thenReturn(UUID.randomUUID());
        when(otherUser.getUsername()).thenReturn("otherUser");
        when(post.getUser()).thenReturn(author);
        long timestamp = System.currentTimeMillis();
        Reply publicReply = new Reply(user, "Public reply", timestamp, post, true);
        Reply userReply = new Reply(user, "User reply", timestamp, post, false);
        Reply userReply2 = new Reply(user, "User reply 2", timestamp, post, false);
        Reply otherUserReply = new Reply(otherUser, "Other user reply", timestamp, post, false);
        List<Reply> replies = List.of(publicReply, userReply, userReply2, otherUserReply);
        List<ReplyResponseDto> expectedReplies = getExpectedRepliesAsOtherUser(user,
                otherUser,
                publicReply,
                otherUserReply);
        // Act
        List<ReplyResponseDto> actualReplies = replyService.getReplyResponseDtos(otherUser, replies);

        // Assert
        assertEquals(expectedReplies, actualReplies);
    }

    private static List<ReplyResponseDto> getExpectedRepliesAsUser(User user, Reply publicReply, Reply userReply, Reply userReply2) {
        return List.of(
                new ReplyResponseDto(publicReply.getId(),
                        user.getUsername(),
                        publicReply.getMessage(),
                        publicReply.getTimestamp(),
                        publicReply.getParentPost().getId(),
                        true),
                new ReplyResponseDto(userReply.getId(),
                        user.getUsername(),
                        userReply.getMessage(),
                        userReply.getTimestamp(),
                        userReply.getParentPost().getId(),
                        false),
                new ReplyResponseDto(userReply2.getId(),
                        user.getUsername(),
                        userReply2.getMessage(),
                        userReply2.getTimestamp(),
                        userReply2.getParentPost().getId(),
                        false)
        );
    }

    private static List<ReplyResponseDto> getExpectedRepliesAsAuthor(User user,
                                                                     User otherUser,
                                                                     Reply publicReply,
                                                                     Reply userReply,
                                                                     Reply userReply2,
                                                                     Reply otherUserReply) {
        return List.of(
                new ReplyResponseDto(publicReply.getId(),
                        user.getUsername(),
                        publicReply.getMessage(),
                        publicReply.getTimestamp(),
                        publicReply.getParentPost().getId(),
                        true),
                new ReplyResponseDto(userReply.getId(),
                        user.getUsername(),
                        userReply.getMessage(),
                        userReply.getTimestamp(),
                        userReply.getParentPost().getId(),
                        false),
                new ReplyResponseDto(userReply2.getId(),
                        user.getUsername(),
                        userReply2.getMessage(),
                        userReply2.getTimestamp(),
                        userReply2.getParentPost().getId(),
                        false),
                new ReplyResponseDto(otherUserReply.getId(),
                        otherUser.getUsername(),
                        otherUserReply.getMessage(),
                        otherUserReply.getTimestamp(),
                        otherUserReply.getParentPost().getId(),
                        false)
        );
    }

    private static List<ReplyResponseDto> getExpectedRepliesAsOtherUser(User user,
                                                                     User otherUser,
                                                                     Reply publicReply,
                                                                     Reply otherUserReply) {
        return List.of(
                new ReplyResponseDto(publicReply.getId(),
                        user.getUsername(),
                        publicReply.getMessage(),
                        publicReply.getTimestamp(),
                        publicReply.getParentPost().getId(),
                        true),
                new ReplyResponseDto(otherUserReply.getId(),
                        otherUser.getUsername(),
                        otherUserReply.getMessage(),
                        otherUserReply.getTimestamp(),
                        otherUserReply.getParentPost().getId(),
                        false)
        );
    }
}

