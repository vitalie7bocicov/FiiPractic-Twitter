package ro.info.iasi.fiipractic.twitter.dto.response;

import java.time.Instant;
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

}
