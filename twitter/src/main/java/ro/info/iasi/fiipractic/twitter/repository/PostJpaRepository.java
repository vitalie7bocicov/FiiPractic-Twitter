package ro.info.iasi.fiipractic.twitter.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ro.info.iasi.fiipractic.twitter.model.Post;
import ro.info.iasi.fiipractic.twitter.model.User;

import java.util.List;
import java.util.UUID;

@Repository
public interface PostJpaRepository extends JpaRepository<Post, UUID> {
    List<Post> getPostsByUser(User user);

    List<Post> getPostsByUserId(UUID id);
}
