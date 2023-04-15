package ro.info.iasi.fiipractic.twitter.service;

import org.springframework.stereotype.Service;
import ro.info.iasi.fiipractic.twitter.exception.BadRequestException;
import ro.info.iasi.fiipractic.twitter.model.Mention;
import ro.info.iasi.fiipractic.twitter.model.User;
import ro.info.iasi.fiipractic.twitter.repository.MentionJpaRepository;

import java.util.List;

@Service
public class MentionService {

    final MentionJpaRepository mentionJpaRepository;

    public MentionService(MentionJpaRepository mentionJpaRepository) {
        this.mentionJpaRepository = mentionJpaRepository;
    }

    public Mention saveMention(Mention mention){
        if(mentionJpaRepository.findByUserAndPost(mention.getUser(), mention.getPost())!=null){
            throw new BadRequestException("The user is already mentioned in this post.");
        }
        return mentionJpaRepository.save(mention);
    }

    public List<Mention> getMentionsByUser(User user) {
        return mentionJpaRepository.findMentionsByUser(user);
    }
}
