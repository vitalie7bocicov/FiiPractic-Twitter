package ro.info.iasi.fiipractic.twitter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ro.info.iasi.fiipractic.twitter.model.Follow.Follow;
import ro.info.iasi.fiipractic.twitter.model.Follow.FollowId;
import ro.info.iasi.fiipractic.twitter.model.Like;
import ro.info.iasi.fiipractic.twitter.model.Mention;
import ro.info.iasi.fiipractic.twitter.model.Post;
import ro.info.iasi.fiipractic.twitter.model.User;
import ro.info.iasi.fiipractic.twitter.service.*;

@Component
public class CommandLineRunner implements org.springframework.boot.CommandLineRunner {

    @Autowired
    UserService userService;

    @Autowired
    PostService postService;

    @Autowired
    FollowService followService;

    @Autowired
    LikeService likeService;

    @Autowired
    MentionService mentionService;

    public User getUser(){
        return new User("Vitalie", "Bocicov", "1234", "vitalie@gmail.com");
    }

    public Post getPost(User user, String message){
        return new Post(user, message, System.currentTimeMillis());
    }

    @Override
    public void run(String... args) throws Exception {

        User user = userService.saveUser(getUser());
        User follower = new User("Carl", "B", "132141", "asdgasdg");
        User savedFollower = userService.saveUser(follower);

        followService.saveFollow(user, savedFollower);
        followService.saveFollow(savedFollower, user);
        Post post = postService.savePost(getPost(user, "CARLITO!"));
        Like like = new Like(user, post);
        Like like2 = new Like(savedFollower, post);
        likeService.saveLike(like);
        likeService.saveLike(like2);

        Mention mention = new Mention(user, post);
        mentionService.saveMention(mention);
        mentionService.saveMention(mention);

    }
}