package ro.info.iasi.fiipractic.twitter.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.info.iasi.fiipractic.twitter.model.Follow.Follow;
import ro.info.iasi.fiipractic.twitter.model.Follow.FollowId;
import ro.info.iasi.fiipractic.twitter.model.User;
import ro.info.iasi.fiipractic.twitter.repository.FollowJpaRepository;

@Service
public class FollowService {

    @Autowired
    FollowJpaRepository followJpaRepository;

    public Follow saveFollow(User user, User following){
        FollowId followId = new FollowId();
        followId.setUserId(user.getId());
        followId.setFollowingId(following.getId());
        Follow follow = new Follow(followId, user,  following, System.currentTimeMillis());
        return followJpaRepository.save(follow);
    }
}
