package ro.info.iasi.fiipractic.twitter.dto.request;

import jakarta.validation.constraints.NotBlank;

public class FollowRequestDto {

    @NotBlank(message = "username is required.")
    String username;

    @NotBlank(message = "usernameToFollow is required.")
    String usernameToFollow;


    public FollowRequestDto(String username, String usernameToFollow) {
        this.username = username;
        this.usernameToFollow = usernameToFollow;
    }

    public String getUsername() {
        return username;
    }

    public String getUsernameToFollow() {
        return usernameToFollow;
    }
}
