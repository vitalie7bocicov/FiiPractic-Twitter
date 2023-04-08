package ro.info.iasi.fiipractic.twitter.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.info.iasi.fiipractic.twitter.model.Like;
import ro.info.iasi.fiipractic.twitter.repository.LikeJpaRepository;

@Service
public class LikeService {

    @Autowired
    LikeJpaRepository likeJpaRepository;

    public Like saveLike(Like like){
        return likeJpaRepository.save(like);
    }
}
