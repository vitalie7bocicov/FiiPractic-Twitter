package ro.info.iasi.fiipractic.twitter.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.info.iasi.fiipractic.twitter.dto.request.PostCRUDRequestDto;
import ro.info.iasi.fiipractic.twitter.dto.request.PostRequestDto;
import ro.info.iasi.fiipractic.twitter.dto.response.PostLikesResponseDto;
import ro.info.iasi.fiipractic.twitter.dto.response.PostResponseDto;
import ro.info.iasi.fiipractic.twitter.model.Mention;
import ro.info.iasi.fiipractic.twitter.model.Post;
import ro.info.iasi.fiipractic.twitter.model.User;
import ro.info.iasi.fiipractic.twitter.service.MentionService;
import ro.info.iasi.fiipractic.twitter.service.PostService;
import ro.info.iasi.fiipractic.twitter.service.UserService;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping(("/posts"))
public class PostController {
    private final UserService userService;
    private final PostService postService;
    private final MentionService mentionService;

    public PostController(UserService userService, PostService postService, MentionService mentionService) {
        this.userService = userService;
        this.postService = postService;
        this.mentionService = mentionService;
    }

    @PostMapping
    public ResponseEntity<String> savePost(@Valid @RequestBody PostRequestDto postRequestDto){
        User user = userService.getByUsername(postRequestDto.getUsername());
        Post post = new Post(user, postRequestDto.getMessage(), System.currentTimeMillis());
        postService.savePost(post);
        return ResponseEntity.ok("The post has been successfully created!");
    }

    @PostMapping("/repost")
    public ResponseEntity<String> repost(@RequestBody PostCRUDRequestDto postDto){
        User user = userService.getByUsername(postDto.getUsername());
        Post post = postService.getPostById(postDto.getPostId());
        Post repostedPost = new Post(user, post.getMessage(), System.currentTimeMillis());
        postService.savePost(repostedPost);
        return ResponseEntity.ok("The post has been successfully reposted!");
    }

    @DeleteMapping
    public ResponseEntity<String> deletePost(@RequestBody PostCRUDRequestDto deletePostDto){
        User user = userService.getByUsername(deletePostDto.getUsername());
        Post post = postService.getPostById(deletePostDto.getPostId());
        postService.deletePost(user, post);
        return ResponseEntity.ok("The post has been deleted!");
    }

    @GetMapping
    public ResponseEntity<List<PostLikesResponseDto>> getOwnPosts(@RequestParam String username,
                                                                  @RequestParam (required = false) String timestamp){
        Instant instant;
        User user = userService.getByUsername(username);
        List<Post> posts;
        if (timestamp != null) {
            instant = Instant.parse(timestamp);
            long timestampInMillis = instant.toEpochMilli();
            posts = postService.getPostsByUserWithTimeFilter(user, timestampInMillis);
        }
        else {
            posts = postService.getPostsByUser(user);
        }
        var postLikesResponseDto = postService.getPostsWithLikes(posts);
        if (postLikesResponseDto.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(postLikesResponseDto);
    }

    @GetMapping("/feed")
    public ResponseEntity<List<PostResponseDto>> getFeed(@RequestParam String username){
        User user = userService.getByUsername(username);
        List<Post> posts = postService.getFeed(user);
        var responsePosts = postService.getPostResponseDtos(posts);
        if (responsePosts.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(responsePosts);
    }

    @PostMapping("/mentions")
    public ResponseEntity<String> saveMention(@RequestBody PostCRUDRequestDto mentionDto){
        User user = userService.getByUsername(mentionDto.getUsername());
        Post post = postService.getPostById(mentionDto.getPostId());
        Mention mention = new Mention(user, post);
        mentionService.saveMention(mention);
        return ResponseEntity.ok("The user has been successfully mentioned in the post!");
    }

    @GetMapping("/mentions")
    public ResponseEntity<List<PostResponseDto>> getAllMentions(@RequestParam String username){
        User user = userService.getByUsername(username);
        List<Mention> mentions = mentionService.getMentionsByUser(user);
        List<Post> posts = mentions.stream().map(Mention::getPost).toList();
        var responsePosts = postService.getPostResponseDtos(posts);
        if (responsePosts.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(responsePosts);
    }
}
