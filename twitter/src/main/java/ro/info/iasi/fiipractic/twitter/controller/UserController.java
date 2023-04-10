package ro.info.iasi.fiipractic.twitter.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.info.iasi.fiipractic.twitter.dto.request.UserRequestDto;
import ro.info.iasi.fiipractic.twitter.dto.response.UserResponseDto;
import ro.info.iasi.fiipractic.twitter.model.User;
import ro.info.iasi.fiipractic.twitter.service.UserService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@Valid @RequestBody UserRequestDto userRequestDto){
        User user = new User(userRequestDto.getUsername(),
                userRequestDto.getFirstname(),
                userRequestDto.getLastname(),
                userRequestDto.getEmail(),
                userRequestDto.getEmail());
        userService.saveUser(user);

        return ResponseEntity.ok("User " + user.getId() + " has been registered successfully!");
    }

    @GetMapping("/search")
    public ResponseEntity<List<UserResponseDto>> searchUsers(
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String firstname,
            @RequestParam(required = false) String lastname) {
        List<User> foundUsers = userService.searchUsers(username, firstname, lastname);
        List<UserResponseDto> users = foundUsers.stream()
                .map(user -> new UserResponseDto(user.getUsername(), user.getFirstname(), user.getLastname()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(users);
    }
}
