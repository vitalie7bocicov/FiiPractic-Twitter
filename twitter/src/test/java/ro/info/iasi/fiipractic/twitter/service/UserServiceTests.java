package ro.info.iasi.fiipractic.twitter.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import ro.info.iasi.fiipractic.twitter.exception.NotFoundException;
import ro.info.iasi.fiipractic.twitter.exception.UsernameTakenException;
import ro.info.iasi.fiipractic.twitter.model.User;
import ro.info.iasi.fiipractic.twitter.repository.UserJpaRepository;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;


@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class UserServiceTests {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserJpaRepository userRepository;

    private User user;

    @BeforeEach
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
