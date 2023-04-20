package ro.info.iasi.fiipractic.twitter.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import ro.info.iasi.fiipractic.twitter.exception.BadRequestException;
import ro.info.iasi.fiipractic.twitter.model.User;
import ro.info.iasi.fiipractic.twitter.model.follow.Follow;
import ro.info.iasi.fiipractic.twitter.model.follow.FollowId;
import ro.info.iasi.fiipractic.twitter.repository.FollowJpaRepository;
import ro.info.iasi.fiipractic.twitter.repository.UserJpaRepository;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class FollowServiceTests {

    @InjectMocks
    private FollowService followService;

    @Mock
    private UserJpaRepository  userJpaRepository;

    @Mock
    private FollowJpaRepository followJpaRepository;

    @Mock
    User user;

    @Mock
    User userToFollow;

    FollowId followId;

    Follow follow;


    @BeforeEach
    void setUp() {
        followService = new FollowService(followJpaRepository, userJpaRepository);
        when(user.getId()).thenReturn(UUID.randomUUID());
        when(userToFollow.getId()).thenReturn(UUID.randomUUID());
        followId = new FollowId(user.getId(), userToFollow.getId());
        follow = new Follow(followId, user, userToFollow, System.currentTimeMillis());
    }

    @Test
    void testSaveFollow() {
        when(user.getUsername()).thenReturn("username");
        when(userToFollow.getUsername()).thenReturn("usernameToFollow");
        when(followJpaRepository.save(any(Follow.class))).thenReturn(follow);
        Follow result = followService.saveFollow(user, userToFollow);
        assertEquals(follow, result);
    }

    @Test
    void testSaveFollowWithSameUser() {
        when(user.getUsername()).thenReturn("username");
        when(userToFollow.getUsername()).thenReturn("usernameToFollow");
        when(userToFollow.getUsername()).thenReturn("username");
        assertThrows(BadRequestException.class,  () -> followService.saveFollow(user, userToFollow));
    }

    @Test
    void testSaveFollowAlreadyExists() {
        when(user.getUsername()).thenReturn("username");
        when(userToFollow.getUsername()).thenReturn("usernameToFollow");
        when(followJpaRepository.findFollowByUserAndFollowed(user, userToFollow)).thenReturn(new Follow());
        assertThrows(BadRequestException.class,  () -> followService.saveFollow(user, userToFollow));
    }

    @Test
    void testGetFollowedUsers() {
        List<User> followedUsers = List.of(userToFollow);
        when(followJpaRepository.findByUser(user)).thenReturn(List.of(follow));
        List<User> result = followService.getFollowedUsers(user);
        assertEquals(followedUsers, result);
    }

    @Test
    void testUnfollow() {
        when(followJpaRepository.findFollowByUserAndFollowed(user, userToFollow)).thenReturn(follow);
        doNothing().when(followJpaRepository).delete(follow);
        followService.unFollow(user, userToFollow);
        verify(followJpaRepository, times(1)).delete(follow);
    }

    @Test
    void testUnfollowNotFound() {
        when(user.getUsername()).thenReturn("username");
        when(userToFollow.getUsername()).thenReturn("usernameToFollow");
        when(followJpaRepository.findFollowByUserAndFollowed(user, userToFollow)).thenReturn(null);
        assertThrows(BadRequestException.class,  () -> followService.unFollow(user, userToFollow));
    }
}
