package ro.info.iasi.fiipractic.twitter.dto.request;

import jakarta.validation.constraints.NotBlank;

public class FollowRequestDto {

    @NotBlank(message = "username is required.")
    String username;

    @NotBlank(message = "usernameToFollow is required.")
    String usernameToFollow;

    public String getUsername() {
        return username;
    }

    public String getUsernameToFollow() {
        return usernameToFollow;
    }
}
