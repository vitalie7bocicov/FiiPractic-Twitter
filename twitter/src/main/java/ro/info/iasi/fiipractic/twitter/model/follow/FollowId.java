package ro.info.iasi.fiipractic.twitter.model.follow;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Embeddable
public class FollowId implements Serializable {

    private UUID userId;

    private UUID followedId;

    public FollowId() {
    }

    public FollowId(UUID userId, UUID followed) {
        this.userId = userId;
        this.followedId = followed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FollowId followId = (FollowId) o;
        return Objects.equals(userId, followId.userId) && Objects.equals(followedId, followId.followedId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, followedId);
    }

}
