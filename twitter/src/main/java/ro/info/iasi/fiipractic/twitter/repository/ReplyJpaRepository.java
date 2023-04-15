package ro.info.iasi.fiipractic.twitter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.info.iasi.fiipractic.twitter.model.Reply;

import java.util.List;
import java.util.UUID;

public interface ReplyJpaRepository extends JpaRepository<Reply, UUID>
{
    List<Reply> findRepliesByParentPostId(UUID id);
}
