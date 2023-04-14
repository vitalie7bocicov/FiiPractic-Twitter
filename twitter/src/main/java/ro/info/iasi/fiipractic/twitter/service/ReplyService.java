package ro.info.iasi.fiipractic.twitter.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.info.iasi.fiipractic.twitter.model.Reply;
import ro.info.iasi.fiipractic.twitter.repository.ReplyJpaRepository;

@Service
public class ReplyService {

    final ReplyJpaRepository replyJpaRepository;

    public ReplyService(ReplyJpaRepository replyJpaRepository) {
        this.replyJpaRepository = replyJpaRepository;
    }


    public Reply saveReply(Reply reply){
        return replyJpaRepository.save(reply);
    }
}
