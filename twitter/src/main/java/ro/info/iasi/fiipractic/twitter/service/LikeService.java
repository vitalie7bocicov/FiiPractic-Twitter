package ro.info.iasi.fiipractic.twitter.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.info.iasi.fiipractic.twitter.model.Like;
import ro.info.iasi.fiipractic.twitter.repository.LikeJpaRepository;

@Service
public class LikeService {


    final LikeJpaRepository likeJpaRepository;

    public LikeService(LikeJpaRepository likeJpaRepository) {
        this.likeJpaRepository = likeJpaRepository;
    }

    public Like saveLike(Like like){
        return likeJpaRepository.save(like);
    }
}
