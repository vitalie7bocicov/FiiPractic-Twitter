package ro.info.iasi.fiipractic.twitter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ro.info.iasi.fiipractic.twitter.model.User;

import java.util.UUID;
@Repository
public interface UserJpaRepository extends JpaRepository<User, UUID> {
    User findByFirstname(String firstname);
}
