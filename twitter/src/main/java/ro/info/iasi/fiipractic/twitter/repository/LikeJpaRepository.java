package ro.info.iasi.fiipractic.twitter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ro.info.iasi.fiipractic.twitter.model.Like;

import java.util.UUID;

@Repository
public interface LikeJpaRepository extends JpaRepository<Like, UUID> {
}
