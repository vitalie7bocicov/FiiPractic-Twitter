package ro.info.iasi.fiipractic.twitter.dto.request;

import jakarta.validation.constraints.NotBlank;

public class UnfollowRequestDto {

    @NotBlank(message = "username is required.")
    String username;

    @NotBlank(message = "usernameToUnfollow is required.")
    String usernameToUnfollow;

    public UnfollowRequestDto(String username, String usernameToUnfollow) {
        this.username = username;
        this.usernameToUnfollow = usernameToUnfollow;
    }

    public String getUsername() {
        return username;
    }

    public String getUsernameToUnfollow() {
        return usernameToUnfollow;
    }
}
