package ro.info.iasi.fiipractic.twitter.serviceTest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import ro.info.iasi.fiipractic.twitter.exception.BadRequestException;
import ro.info.iasi.fiipractic.twitter.exception.FollowRelationshipAlreadyExistsException;
import ro.info.iasi.fiipractic.twitter.exception.FollowRelationshipNotFound;
import ro.info.iasi.fiipractic.twitter.model.Follow.Follow;
import ro.info.iasi.fiipractic.twitter.model.Follow.FollowId;
import ro.info.iasi.fiipractic.twitter.model.User;
import ro.info.iasi.fiipractic.twitter.repository.FollowJpaRepository;
import ro.info.iasi.fiipractic.twitter.repository.UserJpaRepository;
import ro.info.iasi.fiipractic.twitter.service.FollowService;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class FollowServiceTest {

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


    @Before
    public void setUp() {
        followService = new FollowService(followJpaRepository, userJpaRepository);
        when(user.getId()).thenReturn(UUID.randomUUID());
        when(userToFollow.getId()).thenReturn(UUID.randomUUID());
        when(user.getUsername()).thenReturn("username");
        when(userToFollow.getUsername()).thenReturn("usernameToFollow");
        followId = new FollowId(user.getId(), userToFollow.getId());
        follow = new Follow(followId, user, userToFollow, System.currentTimeMillis());
    }

    @Test
    public void testSaveFollow() {
        when(followJpaRepository.save(any(Follow.class))).thenReturn(follow);
        Follow result = followService.saveFollow(user, userToFollow);
        assertEquals(follow, result);
    }

    @Test
    public void testSaveFollowWithSameUser() {
        when(userToFollow.getUsername()).thenReturn("username");
        assertThrows(BadRequestException.class,  () -> followService.saveFollow(user, userToFollow));
    }

    @Test
    public void testSaveFollowAlreadyExists() {
        when(followJpaRepository.findFollowByUserAndFollowed(user, userToFollow)).thenReturn(new Follow());
        assertThrows(FollowRelationshipAlreadyExistsException.class,  () -> followService.saveFollow(user, userToFollow));
    }

    @Test
    public void testGetFollowedUsers() {
        List<User> followedUsers = List.of(userToFollow);
        when(followJpaRepository.findByUser(user)).thenReturn(List.of(follow));
        List<User> result = followService.getFollowedUsers(user);
        assertEquals(followedUsers, result);
    }

    @Test
    public void testUnfollow() {
        when(followJpaRepository.findFollowByUserAndFollowed(user, userToFollow)).thenReturn(follow);
        doNothing().when(followJpaRepository).delete(follow);
        followService.unFollow(user, userToFollow);
        verify(followJpaRepository, times(1)).delete(follow);
    }

    @Test
    public void testUnfollowNotFound() {
        when(followJpaRepository.findFollowByUserAndFollowed(user, userToFollow)).thenReturn(null);
        assertThrows(FollowRelationshipNotFound.class,  () -> followService.unFollow(user, userToFollow));
    }
}
