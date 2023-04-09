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
    @MapsId("followingId")
    @JoinColumn(name = "following_id", referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "fk_follow_following",
                    foreignKeyDefinition = "FOREIGN KEY (following_id) REFERENCES users(id) ON DELETE CASCADE"))
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

