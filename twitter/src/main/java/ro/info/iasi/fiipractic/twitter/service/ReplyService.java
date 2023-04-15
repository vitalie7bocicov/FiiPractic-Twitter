package ro.info.iasi.fiipractic.twitter.service;

import org.springframework.stereotype.Service;
import ro.info.iasi.fiipractic.twitter.model.Post;
import ro.info.iasi.fiipractic.twitter.model.Reply;
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
}
