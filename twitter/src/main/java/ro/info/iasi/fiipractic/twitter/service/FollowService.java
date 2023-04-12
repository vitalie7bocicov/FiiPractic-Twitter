package ro.info.iasi.fiipractic.twitter.service;

import org.springframework.stereotype.Service;
import ro.info.iasi.fiipractic.twitter.exception.BadRequestException;
import ro.info.iasi.fiipractic.twitter.exception.FollowRelationshipAlreadyExistsException;
import ro.info.iasi.fiipractic.twitter.exception.UserNotFoundException;
import ro.info.iasi.fiipractic.twitter.model.Follow.Follow;
import ro.info.iasi.fiipractic.twitter.model.Follow.FollowId;
import ro.info.iasi.fiipractic.twitter.model.User;
import ro.info.iasi.fiipractic.twitter.repository.FollowJpaRepository;
import ro.info.iasi.fiipractic.twitter.repository.UserJpaRepository;

@Service
public class FollowService {

    final FollowJpaRepository followRepository;
    final UserJpaRepository userRepository;

    public FollowService(FollowJpaRepository followRepository, UserJpaRepository userRepository) {
        this.followRepository = followRepository;
        this.userRepository = userRepository;
    }

    public Follow saveFollow(String username, String usernameToFollow){
        if (username.equals(usernameToFollow))
            throw new BadRequestException(" You cannot follow yourself. Please select a different user to follow.");
        User user = userRepository.findByUsername(username);
        User followed = userRepository.findByUsername(usernameToFollow);
        if (user == null)
            throw new UserNotFoundException("User with username '" + username + "' not found.");
        if (followed == null)
            throw new UserNotFoundException("User with username '" + usernameToFollow + "' not found.");
        if (followRepository.findFollowByUserAndFollowed(user, followed) != null)
            throw new FollowRelationshipAlreadyExistsException();
        FollowId followId = new FollowId(user.getId(), followed.getId());
        Follow follow = new Follow(followId, user, followed, System.currentTimeMillis());
        return followRepository.save(follow);
    }

}
