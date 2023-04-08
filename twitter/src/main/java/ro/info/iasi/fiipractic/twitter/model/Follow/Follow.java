package ro.info.iasi.fiipractic.twitter.model.Follow;

import jakarta.persistence.*;
import ro.info.iasi.fiipractic.twitter.model.User;

@Entity
@Table(name = "followers")
public class Follow {

    @EmbeddedId
    private FollowId id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @MapsId("userId")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @MapsId("followingId")
    private User following;

    private long timestamp;

    public Follow() {
    }

    public Follow(FollowId id, User user, User following, long timestamp) {
        this.id = id;
        this.user = user;
        this.following = following;
        this.timestamp = timestamp;
    }

    public FollowId getId() {
        return id;
    }

    public void setId(FollowId id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getFollowing() {
        return following;
    }

    public void setFollowing(User following) {
        this.following = following;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}

