package ro.info.iasi.fiipractic.twitter.service;

import org.springframework.stereotype.Service;
import ro.info.iasi.fiipractic.twitter.exception.BadRequestException;
import ro.info.iasi.fiipractic.twitter.exception.NotFoundException;
import ro.info.iasi.fiipractic.twitter.model.Like;
import ro.info.iasi.fiipractic.twitter.model.Post;
import ro.info.iasi.fiipractic.twitter.model.User;
import ro.info.iasi.fiipractic.twitter.repository.LikeJpaRepository;

import java.util.List;

@Service
public class LikeService {


    private final LikeJpaRepository likeJpaRepository;

    public LikeService(LikeJpaRepository likeJpaRepository) {
        this.likeJpaRepository = likeJpaRepository;
    }

    public Like saveLike(Like like){
        if (likeJpaRepository.findLikeByUserAndPost(like.getUser(), like.getPost())!=null)
            throw new BadRequestException("'"+ like.getUser().getUsername() +"' already liked this post.");
        return likeJpaRepository.save(like);
    }

    public Like getLikeByUserAndPost(User user, Post post) {
        Like like = likeJpaRepository.findLikeByUserAndPost(user, post);
        if(like == null){
            throw new NotFoundException("Like was not found.");
        }
        return like;
    }

    public void removeLike(Like like) {
        likeJpaRepository.delete(like);
    }

    public List<Like> getLikesByPost(Post post) {
        return likeJpaRepository.findLikesByPost(post);
    }
}
