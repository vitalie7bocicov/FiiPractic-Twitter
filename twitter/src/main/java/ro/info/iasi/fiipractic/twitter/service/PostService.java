package ro.info.iasi.fiipractic.twitter.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.info.iasi.fiipractic.twitter.model.Post;
import ro.info.iasi.fiipractic.twitter.model.User;
import ro.info.iasi.fiipractic.twitter.repository.PostJpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PostService {

    @Autowired
    private PostJpaRepository postJpaRepository;

    public Post savePost(Post post){
        return postJpaRepository.save(post);
    }

    public List<Post> getPostsByUser(User user) {
        Optional<List<Post>> usersPosts = Optional.ofNullable(postJpaRepository.getPostsByUser(user));
        return usersPosts.orElse(null);
    }

    public List<Post> getPostsByUserId(UUID id) {
        Optional<List<Post>> usersPosts = Optional.ofNullable(postJpaRepository.getPostsByUserId(id));
        return usersPosts.orElse(null);
    }
}
