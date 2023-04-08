package ro.info.iasi.fiipractic.twitter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ro.info.iasi.fiipractic.twitter.model.Post;

import java.util.UUID;

@Repository
public interface PostJpaRepository extends JpaRepository<Post, UUID> {
}
