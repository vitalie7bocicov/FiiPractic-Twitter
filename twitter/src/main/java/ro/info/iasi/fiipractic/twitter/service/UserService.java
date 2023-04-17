package ro.info.iasi.fiipractic.twitter.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import ro.info.iasi.fiipractic.twitter.exception.NotFoundException;
import ro.info.iasi.fiipractic.twitter.exception.UsernameTakenException;
import ro.info.iasi.fiipractic.twitter.model.User;
import ro.info.iasi.fiipractic.twitter.repository.UserJpaRepository;

import java.util.List;

@Service
public class UserService {
    private final UserJpaRepository userRepository;
    public UserService(UserJpaRepository userRepository) {
        this.userRepository = userRepository;
    }


    public User saveUser(User user){
        if(userRepository.findByUsername(user.getUsername())!=null)
            throw new UsernameTakenException("Username " + user.getUsername() + " is taken.");
        return userRepository.save(user);
    }

    public User getByUsername(String username) {
        User user = userRepository.findByUsername(username);
        if(user == null)
            throw new NotFoundException("User with username '" + username + "' was not found.");
        return user;
    }

    public List<User> searchUsers(String username, String firstname, String lastname) {
        List<User> foundUsers =
                userRepository.findByUsernameContainingOrFirstnameContainingOrLastnameContaining(username, firstname, lastname);
        if(foundUsers == null || foundUsers.isEmpty())
            throw new NotFoundException("No users found with the specified search criteria.");
        return foundUsers;
    }

    public void deleteUser(User user) {
        userRepository.delete(user);
    }
    @Transactional
    public void updateUser(User user) {
        userRepository.updateUser(user.getId(),
                user.getFirstname(),
                user.getLastname(),
                user.getEmail(),
                user.getPassword());
    }
}
