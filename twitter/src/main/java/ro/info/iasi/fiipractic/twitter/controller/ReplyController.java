package ro.info.iasi.fiipractic.twitter.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.info.iasi.fiipractic.twitter.dto.request.PostCRUDRequestDto;
import ro.info.iasi.fiipractic.twitter.dto.request.ReplyRequestDto;
import ro.info.iasi.fiipractic.twitter.dto.response.ReplyResponseDto;
import ro.info.iasi.fiipractic.twitter.model.Post;
import ro.info.iasi.fiipractic.twitter.model.Reply;
import ro.info.iasi.fiipractic.twitter.model.User;
import ro.info.iasi.fiipractic.twitter.service.PostService;
import ro.info.iasi.fiipractic.twitter.service.ReplyService;
import ro.info.iasi.fiipractic.twitter.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/replies")
public class ReplyController {

    private final UserService userService;
    private final PostService postService;
    private final ReplyService replyService;

    public ReplyController(UserService userService, PostService postService, ReplyService replyService) {
        this.userService = userService;
        this.postService = postService;
        this.replyService = replyService;
    }

    @PostMapping
    public ResponseEntity<String> saveReply(@Valid @RequestBody ReplyRequestDto replyDto){
        User user = userService.getByUsername(replyDto.getUsername());
        Post parentPost = postService.getPostById(replyDto.getParentPostId());
        Reply reply = new Reply(user,
                replyDto.getMessage(),
                System.currentTimeMillis(),
                parentPost,
                replyDto.getIsPublic());
        replyService.saveReply(reply);
        return ResponseEntity.ok("The reply has been successfully created!");
    }

    @GetMapping
    public ResponseEntity<List<ReplyResponseDto>> getReplies(@RequestBody PostCRUDRequestDto replyDto){
        User user = userService.getByUsername(replyDto.getUsername());
        Post post = postService.getPostById(replyDto.getPostId());
        List<Reply> replies = replyService.getRepliesByPost(post);
        var repliesDto = replyService.getReplyResponseDtos(user, replies);
        if (replies.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(repliesDto);
    }
}
