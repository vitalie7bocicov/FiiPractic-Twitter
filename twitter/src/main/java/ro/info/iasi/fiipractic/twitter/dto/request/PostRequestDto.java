package ro.info.iasi.fiipractic.twitter.dto.request;

import jakarta.validation.constraints.NotBlank;

public class PostRequestDto {

    @NotBlank(message = "username is required.")
    private final String username;

    @NotBlank(message = "message is required.")
    private final String message;

    public PostRequestDto(String username, String message) {
        this.username = username;
        this.message = message;
    }

    public String getUsername() {
        return username;
    }

    public String getMessage() {
        return message;
    }
}
