package ro.info.iasi.fiipractic.twitter.dto.request;

import jakarta.validation.constraints.NotBlank;

public class PostRequestDto {

    @NotBlank(message = "username is required.")
    private String username;

    @NotBlank(message = "message is required.")
    private String message;

    public String getUsername() {
        return username;
    }

    public String getMessage() {
        return message;
    }
}
