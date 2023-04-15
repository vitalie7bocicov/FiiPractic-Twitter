package ro.info.iasi.fiipractic.twitter.service;

import org.springframework.stereotype.Service;
import ro.info.iasi.fiipractic.twitter.dto.response.ReplyResponseDto;
import ro.info.iasi.fiipractic.twitter.model.Post;
import ro.info.iasi.fiipractic.twitter.model.Reply;
import ro.info.iasi.fiipractic.twitter.model.User;
import ro.info.iasi.fiipractic.twitter.repository.ReplyJpaRepository;

import java.util.List;

@Service
public class ReplyService {

    final ReplyJpaRepository replyJpaRepository;

    public ReplyService(ReplyJpaRepository replyJpaRepository) {
        this.replyJpaRepository = replyJpaRepository;
    }


    public Reply saveReply(Reply reply){
        return replyJpaRepository.save(reply);
    }

    public List<Reply> getRepliesByPost(Post post) {
        return replyJpaRepository.findRepliesByParentPostId(post.getId());
    }

    public List<ReplyResponseDto> getReplyResponseDtos(User user, List<Reply> replies){
        return replies.stream()
                .filter(reply -> reply.isPublic()
                        || reply.getParentPost().getUser().getId().compareTo(user.getId())==0
                        || reply.getUser().getId().compareTo(user.getId())==0)
                .map(reply -> new ReplyResponseDto(
                        reply.getId(),
                        reply.getUser().getUsername(),
                        reply.getMessage(),
                        System.currentTimeMillis(),
                        reply.getParentPost().getId(),
                        reply.isPublic()))
                .toList();
    }
}
