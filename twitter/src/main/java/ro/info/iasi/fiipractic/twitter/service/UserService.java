package ro.info.iasi.fiipractic.twitter.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import ro.info.iasi.fiipractic.twitter.exception.BadRequestException;
import ro.info.iasi.fiipractic.twitter.exception.NotFoundException;
import ro.info.iasi.fiipractic.twitter.exception.UsernameTakenException;
import ro.info.iasi.fiipractic.twitter.model.User;
import ro.info.iasi.fiipractic.twitter.repository.UserJpaRepository;
import ro.info.iasi.fiipractic.twitter.util.IPasswordEncoder;
import ro.info.iasi.fiipractic.twitter.util.PasswordEncoder;

import java.util.List;

@Service
public class UserService {
    private final UserJpaRepository userRepository;

    private final IPasswordEncoder passwordEncoder;
    public UserService(UserJpaRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }


    public User saveUser(User user){
        if(userRepository.findByUsername(user.getUsername())!=null)
            throw new UsernameTakenException("Username " + user.getUsername() + " is taken.");
        user.setPassword(passwordEncoder.encode(user.getPassword()));
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
    public void deleteUser(User user, String password) {
        if(!passwordEncoder.matches(password, user.getPassword()))
            throw new BadRequestException("Password provided is incorrect.");
        userRepository.delete(user);
    }
    @Transactional
    public void updateUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.updateUser(user.getId(),
                user.getFirstname(),
                user.getLastname(),
                user.getEmail(),
                user.getPassword());
    }

    @Transactional
        public void updateUserPassword(User user, String oldPassword, String newPassword) {
        if(!passwordEncoder.matches(oldPassword, user.getPassword()))
            throw new BadRequestException("Old password is incorrect.");
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.updateUserPassword(user.getId(), user.getPassword());
    }
}
