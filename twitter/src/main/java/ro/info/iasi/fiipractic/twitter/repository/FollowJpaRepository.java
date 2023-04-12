package ro.info.iasi.fiipractic.twitter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ro.info.iasi.fiipractic.twitter.model.Follow.Follow;
import ro.info.iasi.fiipractic.twitter.model.Follow.FollowId;
import ro.info.iasi.fiipractic.twitter.model.User;

@Repository
public interface FollowJpaRepository extends JpaRepository<Follow, FollowId> {
    Follow findFollowByUserAndFollowed(User user, User followed);
}
