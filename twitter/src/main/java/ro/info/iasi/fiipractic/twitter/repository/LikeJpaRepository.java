package ro.info.iasi.fiipractic.twitter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ro.info.iasi.fiipractic.twitter.model.Like;
import ro.info.iasi.fiipractic.twitter.model.Post;
import ro.info.iasi.fiipractic.twitter.model.User;

import java.util.List;
import java.util.UUID;

@Repository
public interface LikeJpaRepository extends JpaRepository<Like, UUID> {

    Like findLikeById(UUID id);

    Like findLikeByUserAndPost(User user, Post post);

    List<Like> findLikesByPost(Post post);
}
