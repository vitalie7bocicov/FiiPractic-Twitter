package ro.info.iasi.fiipractic.twitter.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.info.iasi.fiipractic.twitter.dto.request.PostCRUDRequestDto;
import ro.info.iasi.fiipractic.twitter.dto.request.PostRequestDto;
import ro.info.iasi.fiipractic.twitter.dto.response.PostResponseDto;
import ro.info.iasi.fiipractic.twitter.model.Post;
import ro.info.iasi.fiipractic.twitter.model.User;
import ro.info.iasi.fiipractic.twitter.service.PostService;
import ro.info.iasi.fiipractic.twitter.service.UserService;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping(("/posts"))
public class PostController {
    private final UserService userService;
    private final PostService postService;

    public PostController(UserService userService, PostService postService) {
        this.userService = userService;
        this.postService = postService;
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
    public ResponseEntity<List<PostResponseDto>> getOwnPosts(@RequestParam String username,
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

        List<PostResponseDto> postsResponseDto = posts.stream()
                .map(post -> new PostResponseDto(post.getId(), post.getUser().getUsername(),
                        post.getMessage(),
                        post.getTimestamp())).toList();
        if (postsResponseDto.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(postsResponseDto);
    }

    @GetMapping("/feed")
    public ResponseEntity<List<PostResponseDto>> getFeed(@RequestParam String username){
        User user = userService.getByUsername(username);
        List<Post> posts = postService.getFeed(user);
        List<PostResponseDto> postResponseDtos = posts.stream()
                .map(post -> new PostResponseDto(post.getId(), post.getUser().getUsername(),
                        post.getMessage(),
                        post.getTimestamp())).toList();
        if (postResponseDtos.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(postResponseDtos);
    }

}
