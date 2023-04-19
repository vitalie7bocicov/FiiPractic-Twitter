package ro.info.iasi.fiipractic.twitter.service;

import org.springframework.stereotype.Service;
import ro.info.iasi.fiipractic.twitter.exception.BadRequestException;
import ro.info.iasi.fiipractic.twitter.model.User;
import ro.info.iasi.fiipractic.twitter.model.follow.Follow;
import ro.info.iasi.fiipractic.twitter.model.follow.FollowId;
import ro.info.iasi.fiipractic.twitter.repository.FollowJpaRepository;
import ro.info.iasi.fiipractic.twitter.repository.UserJpaRepository;

import java.util.List;

@Service
public class FollowService {

    final FollowJpaRepository followRepository;
    final UserJpaRepository userRepository;

    public FollowService(FollowJpaRepository followRepository, UserJpaRepository userRepository) {
        this.followRepository = followRepository;
        this.userRepository = userRepository;
    }

    public Follow saveFollow(User user, User userToFollow) {
        if (user.getUsername().equals(userToFollow.getUsername()))
            throw new BadRequestException("You cannot follow yourself. Please select a different user to follow.");
        if (followRepository.findFollowByUserAndFollowed(user, userToFollow) != null)
            throw new BadRequestException("Following relationship already exists.");
        FollowId followId = new FollowId(user.getId(), userToFollow.getId());
        Follow follow = new Follow(followId, user, userToFollow, System.currentTimeMillis());
        return followRepository.save(follow);
    }

    public List<User> getFollowedUsers(User user) {
        List<Follow> follows = followRepository.findByUser(user);
        return follows.stream().map(Follow::getFollowed).toList();
    }

    public void unFollow(User user, User followed) {
        Follow follow = followRepository.findFollowByUserAndFollowed(user, followed);
        if (follow == null)
            throw new BadRequestException("'" + user.getUsername() +
                    "' is not following '" + followed.getUsername() + "'.");
        followRepository.delete(follow);
    }


}
