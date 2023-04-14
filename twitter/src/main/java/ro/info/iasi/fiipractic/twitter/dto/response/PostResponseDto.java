package ro.info.iasi.fiipractic.twitter.dto.response;

import java.time.Instant;

public class PostResponseDto {
    private String username;

    private String message;

    private Instant timestamp;

    public PostResponseDto(String username, String message, long timestamp) {
        this.username = username;
        this.message = message;
        this.timestamp =  Instant.ofEpochMilli(timestamp);
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
}
