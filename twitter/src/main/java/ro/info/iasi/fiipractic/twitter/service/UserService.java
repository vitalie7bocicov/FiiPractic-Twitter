package ro.info.iasi.fiipractic.twitter.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.info.iasi.fiipractic.twitter.exception.UserNotFoundException;
import ro.info.iasi.fiipractic.twitter.exception.UsernameTakenException;
import ro.info.iasi.fiipractic.twitter.model.User;
import ro.info.iasi.fiipractic.twitter.repository.UserJpaRepository;

import java.util.List;
import java.util.Optional;

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

    public List<User> getByFirstname(String firstname) {
        List<User> users = userRepository.findByFirstname(firstname);
        if(users == null || users.isEmpty())
            throw new UserNotFoundException("User with firstname '" + firstname + "' not found.");
        return users;
    }

    public User getByUsername(String username) {
        User user = userRepository.findByUsername(username);

        if(user == null)
            throw new UserNotFoundException("User with username '" + username + "' not found.");
        return user;
    }

    public List<User> searchUsers(String username, String firstname, String lastname) {
        List<User> foundUsers =
                userRepository.findByUsernameContainingOrFirstnameContainingOrLastnameContaining(username, firstname, lastname);
        if(foundUsers == null || foundUsers.isEmpty())
            throw new UserNotFoundException("No users found with the specified search criteria.");
        return foundUsers;
    }
}
