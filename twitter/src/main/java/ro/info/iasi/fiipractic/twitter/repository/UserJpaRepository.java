package ro.info.iasi.fiipractic.twitter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

    @Modifying
    @Query("UPDATE User u SET u.firstname = :firstname, u.lastname = :lastname," +
            " u.email = :email, u.password = :password WHERE u.id = :userId")
    void updateUser(@Param("userId") UUID userId,
                    @Param("firstname") String firstname,
                    @Param("lastname") String lastname,
                    @Param("email") String email,
                    @Param("password") String password);
}
