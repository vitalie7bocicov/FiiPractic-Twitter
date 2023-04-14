package ro.info.iasi.fiipractic.twitter.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.info.iasi.fiipractic.twitter.model.Mention;
import ro.info.iasi.fiipractic.twitter.repository.MentionJpaRepository;

@Service
public class MentionService {

    final MentionJpaRepository mentionJpaRepository;

    public MentionService(MentionJpaRepository mentionJpaRepository) {
        this.mentionJpaRepository = mentionJpaRepository;
    }

    public Mention saveMention(Mention mention){
        return mentionJpaRepository.save(mention);
    }
}
