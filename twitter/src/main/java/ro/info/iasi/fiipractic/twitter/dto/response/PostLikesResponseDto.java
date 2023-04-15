package ro.info.iasi.fiipractic.twitter.dto.response;

import java.time.Instant;
import java.util.List;
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
}
