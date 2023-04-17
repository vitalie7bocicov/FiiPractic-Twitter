package ro.info.iasi.fiipractic.twitter.serviceTest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import ro.info.iasi.fiipractic.twitter.exception.NotFoundException;
import ro.info.iasi.fiipractic.twitter.exception.UsernameTakenException;
import ro.info.iasi.fiipractic.twitter.model.User;
import ro.info.iasi.fiipractic.twitter.repository.UserJpaRepository;
import ro.info.iasi.fiipractic.twitter.service.UserService;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserJpaRepository userRepository;

    private User user;

    @Before
    public void setUp() {
        userService = new UserService(userRepository);
        user = new User("carl",
                "Carl",
                "Johan",
                "carl.johan@gmail.com",
                "password123");
    }

    @Test
    public void testSaveUser() {
        when(userRepository.findByUsername(user.getUsername())).thenReturn(null);
        when(userRepository.save(user)).thenReturn(user);
        User savedUser = userService.saveUser(user);
        assertEquals(user, savedUser);
    }

    @Test
    public void testSaveUserUsernameTaken() {
        when(userRepository.findByUsername(user.getUsername())).thenReturn(user);
        assertThrows(UsernameTakenException.class, () -> userService.saveUser(user));
    }

    @Test
    public void testGetByUsername() {
        when(userRepository.findByUsername(user.getUsername())).thenReturn(user);
        User foundUser = userService.getByUsername(user.getUsername());
        assertEquals(user, foundUser);
    }

    @Test
    public void testGetByUsernameNotFound() {
        when(userRepository.findByUsername(anyString())).thenReturn(null);
        assertThrows(NotFoundException.class, () -> userService.getByUsername("userTest"));
    }

    @Test
    public void testSearchUsers() {
        List<User> expectedUsers = new ArrayList<>();
        expectedUsers.add(user);
        when(userRepository.findByUsernameContainingOrFirstnameContainingOrLastnameContaining(anyString(), anyString(), anyString()))
                .thenReturn(expectedUsers);
        List<User> foundUsers = userService.searchUsers(user.getUsername(), user.getFirstname(), user.getLastname());
        assertEquals(expectedUsers, foundUsers);
    }

    @Test
    public void testSearchUsersNotFound() {
        when(userRepository.findByUsernameContainingOrFirstnameContainingOrLastnameContaining(anyString(), anyString(), anyString()))
                .thenReturn(null);
        assertThrows(NotFoundException.class, () -> userService.searchUsers("userTest", "", ""));
    }

    @Test
    public void testDeleteUser() {
        doNothing().when(userRepository).delete(user);
        userService.deleteUser(user);
        verify(userRepository).delete(user);
    }

    @Test
    public void testUpdateUser() {
        userService.updateUser(user);
        verify(userRepository, times(1)).
                updateUser(user.getId(), user.getFirstname(), user.getLastname(), user.getEmail(), user.getPassword());
    }
}
