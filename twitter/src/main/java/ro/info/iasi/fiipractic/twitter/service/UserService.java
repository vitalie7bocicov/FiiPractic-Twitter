package ro.info.iasi.fiipractic.twitter.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.info.iasi.fiipractic.twitter.model.User;
import ro.info.iasi.fiipractic.twitter.repository.UserJpaRepository;

@Service
public class UserService {

    @Autowired
    private UserJpaRepository userRepository;
    public User saveUser(User user){
        return userRepository.save(user);
    }

}
