package ro.info.iasi.fiipractic.twitter.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UserPasswordRequestDto {

    @NotBlank(message = "Username is required.")
    @Size(min=3, message = "Username must be at least 3 characters long.")
    private String username;

    @NotBlank(message = "Password is required.")
    @Size(min=8, message = "Password must be at least 8 characters long.")
    private String password;

    public UserPasswordRequestDto(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
