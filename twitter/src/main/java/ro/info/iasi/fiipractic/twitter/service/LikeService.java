package ro.info.iasi.fiipractic.twitter.service;

import org.springframework.stereotype.Service;
import ro.info.iasi.fiipractic.twitter.exception.BadRequestException;
import ro.info.iasi.fiipractic.twitter.model.Like;
import ro.info.iasi.fiipractic.twitter.repository.LikeJpaRepository;

@Service
public class LikeService {


    private final LikeJpaRepository likeJpaRepository;

    public LikeService(LikeJpaRepository likeJpaRepository) {
        this.likeJpaRepository = likeJpaRepository;
    }

    public void saveLike(Like like){
        if (likeJpaRepository.findLikeByUserAndPost(like.getUser(), like.getPost())!=null)
            throw new BadRequestException("'"+ like.getUser().getUsername() +"' already liked this post.");
        likeJpaRepository.save(like);
    }
}
