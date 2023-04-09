package ro.info.iasi.fiipractic.twitter.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.info.iasi.fiipractic.twitter.model.User;
import ro.info.iasi.fiipractic.twitter.repository.UserJpaRepository;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserJpaRepository userRepository;
    public User saveUser(User user){
        return userRepository.save(user);
    }

    public User getByFirstname(String firstname) {
        Optional<User> optionalUser = Optional.ofNullable(userRepository.findByFirstname(firstname));
        return optionalUser.orElse(null);
    }
}
