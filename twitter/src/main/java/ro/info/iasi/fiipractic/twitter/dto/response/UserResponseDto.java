package ro.info.iasi.fiipractic.twitter.dto.response;

public class UserResponseDto {

    private String username;
    private String firstname;
    private String lastname;

    public UserResponseDto(String username, String firstname, String lastname) {
        this.username = username;
        this.firstname = firstname;
        this.lastname = lastname;
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
}
