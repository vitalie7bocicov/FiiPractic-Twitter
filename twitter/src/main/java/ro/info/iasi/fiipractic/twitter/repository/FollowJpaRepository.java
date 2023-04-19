package ro.info.iasi.fiipractic.twitter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ro.info.iasi.fiipractic.twitter.model.follow.Follow;
import ro.info.iasi.fiipractic.twitter.model.follow.FollowId;
import ro.info.iasi.fiipractic.twitter.model.User;

import java.util.List;

@Repository
public interface FollowJpaRepository extends JpaRepository<Follow, FollowId> {
    Follow findFollowByUserAndFollowed(User user, User followed);

    List<Follow> findByUser(User user);
}
