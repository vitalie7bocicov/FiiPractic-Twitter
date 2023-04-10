package ro.info.iasi.fiipractic.twitter.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UserRequestDto {

    @NotBlank(message = "Username is required.")
    @Size(min=3, message = "Username must be at least 3 characters long.")
    private String username;

    @NotBlank(message = "Firstname is required.")
    @Size(min=3, message = "Firstname must be at least 3 characters long.")
    private String firstname;

    @NotBlank(message = "Lastname is required.")
    @Size(min=3, message = "Lastname must be at least 1 character long.")
    private String lastname;

    @Email
    private String email;

    @NotBlank(message = "Password is required.")
    @Size(min=8, message = "Password must be at least 8 characters long.")
    private String password;

    public UserRequestDto(String username, String firstname, String lastname, String email, String password) {
        this.username = username;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
