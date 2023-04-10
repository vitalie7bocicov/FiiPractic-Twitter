package ro.info.iasi.fiipractic.twitter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ro.info.iasi.fiipractic.twitter.model.User;

import java.util.List;
import java.util.UUID;
@Repository
public interface UserJpaRepository extends JpaRepository<User, UUID> {
    User findByUsername(String username);
    List<User> findByFirstname(String firstname);

    List<User> findByLastname(String lastname);

    List<User> findByUsernameContainingOrFirstnameContainingOrLastnameContaining(String username, String firstname, String lastname);
}
