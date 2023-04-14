package ro.info.iasi.fiipractic.twitter.service;

import org.springframework.stereotype.Service;
import ro.info.iasi.fiipractic.twitter.model.Post;
import ro.info.iasi.fiipractic.twitter.model.User;
import ro.info.iasi.fiipractic.twitter.repository.PostJpaRepository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class PostService {

    private final PostJpaRepository postJpaRepository;

    private final FollowService followService;
    public PostService(PostJpaRepository postJpaRepository, FollowService followService) {
        this.postJpaRepository = postJpaRepository;
        this.followService = followService;
    }

    public Post savePost(Post post){
        return postJpaRepository.save(post);
    }

    public List<Post> getPostsByUser(User user) {
        return postJpaRepository.getPostsByUser(user);
    }

    public List<Post> getPostsByUserWithTimeFilter(User user, long timestamp) {
        return postJpaRepository.getPostsByUserWithTimeFilter(user, timestamp);
    }

    public List<Post> getFeed(User user) {
        List<User> followedUsers = followService.getFollowedUsers(user);
        List<Post> feed = new ArrayList<>();
        for(User followedUser : followedUsers){
            List<Post> posts = postJpaRepository.getPostsByUser(followedUser);
            feed.addAll(posts);
        }

        Comparator<Post> byTimestampDesc = Comparator.comparing(Post::getTimestamp).reversed();
        feed.sort(byTimestampDesc);

        return feed;
    }
}
