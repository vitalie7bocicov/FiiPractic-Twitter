package ro.info.iasi.fiipractic.twitter.dto.response;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class PostLikesResponseDto {

    private final UUID id;
    private final String username;

    private final String message;

    private final Instant timestamp;

    private List<String> likes;

    public PostLikesResponseDto(UUID id, String username, String message, long timestamp) {
        this.id = id;
        this.username = username;
        this.message = message;
        this.timestamp =  Instant.ofEpochMilli(timestamp);
    }

    public UUID getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getMessage() {
        return message;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public List<String> getLikes() {
        return likes;
    }

    public void setLikes(List<String> likes) {
        this.likes = likes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PostLikesResponseDto that)) return false;
        return Objects.equals(getId(), that.getId()) && Objects.equals(getUsername(), that.getUsername()) && Objects.equals(getMessage(), that.getMessage()) && Objects.equals(getTimestamp(), that.getTimestamp()) && Objects.equals(getLikes(), that.getLikes());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getUsername(), getMessage(), getTimestamp(), getLikes());
    }

    @Override
    public String toString() {
        return "PostLikesResponseDto{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", message='" + message + '\'' +
                ", timestamp=" + timestamp +
                ", likes=" + likes +
                '}';
    }
}
