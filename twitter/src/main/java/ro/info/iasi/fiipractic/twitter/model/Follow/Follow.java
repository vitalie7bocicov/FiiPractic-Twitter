package ro.info.iasi.fiipractic.twitter.model.Follow;

import jakarta.persistence.*;
import ro.info.iasi.fiipractic.twitter.model.User;

@Entity
@Table(name = "followers")
public class Follow {

    @EmbeddedId
    private FollowId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    @JoinColumn(name = "user_id", referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "fk_follow_user",
                    foreignKeyDefinition = "FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE"))
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("followedId")
    @JoinColumn(name = "followed_id", referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "fk_follow_followed",
                    foreignKeyDefinition = "FOREIGN KEY (followed_id) REFERENCES users(id) ON DELETE CASCADE"))
    private User followed;

    private long timestamp;

    public Follow() {
    }

    public Follow(FollowId id, User user, User followed, long timestamp) {
        this.id = id;
        this.user = user;
        this.followed = followed;
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

    public User getFollowed() {
        return followed;
    }

    public void setFollowing(User following) {
        this.followed = followed;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}

