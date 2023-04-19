package ro.info.iasi.fiipractic.twitter.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UserPasswordUpdateRequestDto {

    @NotBlank(message = "Username is required.")
    @Size(min=3, message = "Username must be at least 3 characters long.")
    private String username;

    @NotBlank(message = "Password is required.")
    @Size(min=8, message = "Password must be at least 8 characters long.")
    private String oldPassword;


    @NotBlank(message = "Password is required.")
    @Size(min=8, message = "Password must be at least 8 characters long.")
    private String newPassword;

    public UserPasswordUpdateRequestDto(String username, String oldPassword, String newPassword) {
        this.username = username;
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
    }

    public String getUsername() {
        return username;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }
}
