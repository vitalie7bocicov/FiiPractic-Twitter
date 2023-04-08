package ro.info.iasi.fiipractic.twitter.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.info.iasi.fiipractic.twitter.model.Post;
import ro.info.iasi.fiipractic.twitter.model.User;
import ro.info.iasi.fiipractic.twitter.repository.PostJpaRepository;

@Service
public class PostService {

    @Autowired
    private PostJpaRepository postJpaRepository;

    public Post savePost(Post post){
        return postJpaRepository.save(post);
    }
}
