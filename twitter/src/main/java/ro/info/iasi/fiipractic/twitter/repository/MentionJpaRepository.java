package ro.info.iasi.fiipractic.twitter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ro.info.iasi.fiipractic.twitter.model.Mention;
import ro.info.iasi.fiipractic.twitter.model.Post;
import ro.info.iasi.fiipractic.twitter.model.User;

import java.util.List;
import java.util.UUID;

@Repository
public interface MentionJpaRepository extends JpaRepository<Mention, UUID> {

    Mention findByUserAndPost(User user, Post post);

    List<Mention> findMentionsByUser(User user);
}
