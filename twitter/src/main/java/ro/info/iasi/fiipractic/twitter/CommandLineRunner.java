package ro.info.iasi.fiipractic.twitter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ro.info.iasi.fiipractic.twitter.model.*;
import ro.info.iasi.fiipractic.twitter.model.Follow.Follow;
import ro.info.iasi.fiipractic.twitter.model.Follow.FollowId;
import ro.info.iasi.fiipractic.twitter.service.*;

import java.util.List;
import java.util.UUID;

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

    @Autowired
    ReplyService replyService;

    public User getUser(){
        return new User("Vitalie", "Bocicov", "vitalie@gmail.com", "pass123");
    }

    public Post getPost(User user, String message){
        return new Post(user, message, System.currentTimeMillis());
    }

    @Override
    public void run(String... args) throws Exception {

//        User user = userService.saveUser(getUser());
//        User follower = new User("Carl", "B", "carl@gmail.com", "asdgasdg");
//        User savedFollower = userService.saveUser(follower);
//
//        followService.saveFollow(user, savedFollower);
//        followService.saveFollow(savedFollower, user);
//        Post post = postService.savePost(getPost(user, "CARLITO!"));
//        Like like = new Like(user, post);
//        Like like2 = new Like(savedFollower, post);
//        likeService.saveLike(like);
//        likeService.saveLike(like2);
//
//        Mention mention = new Mention(user, post);
//        mentionService.saveMention(mention);
//        mentionService.saveMention(mention);
//
//        Reply reply = new Reply(user, "Hello", System.currentTimeMillis(), post, true);
//        replyService.saveReply(reply);
//        Reply reply2 = new Reply(savedFollower, "Hello2", System.currentTimeMillis(), post, false);
//        replyService.saveReply(reply2);
//        User user = userService.getByFirstname("Vitalie");
//        System.out.println(user);
        List<Post> usersPosts = postService.getPostsByUserId(UUID.fromString("07c298fd-fe1e-4d60-80f3-ead2fda3ed07"));
        System.out.println(usersPosts);
    }
}