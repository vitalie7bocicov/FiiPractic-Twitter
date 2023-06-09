package ro.info.iasi.fiipractic.twitter.dto.request;

import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

public class PostCRUDRequestDto {

    @NotBlank(message = "username is required.")
    private final String username;

    @NotBlank(message = "postId is required.")
    private final UUID postId;


    public PostCRUDRequestDto(String username, UUID postId) {
        this.username = username;
        this.postId = postId;
    }

    public String getUsername() {
        return username;
    }

    public UUID getPostId() {
        return postId;
    }
}
