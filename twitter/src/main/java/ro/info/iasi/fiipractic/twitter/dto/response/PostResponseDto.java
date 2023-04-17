package ro.info.iasi.fiipractic.twitter.dto.response;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public class PostResponseDto {

    private final UUID id;
    private final String username;

    private final String message;

    private final Instant timestamp;

    public PostResponseDto(UUID id, String username, String message, long timestamp) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PostResponseDto that)) return false;
        return Objects.equals(getId(), that.getId()) && Objects.equals(getUsername(), that.getUsername()) && Objects.equals(getMessage(), that.getMessage()) && Objects.equals(timestamp, that.timestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getUsername(), getMessage(), timestamp);
    }

    @Override
    public String toString() {
        return "PostResponseDto{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", message='" + message + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
