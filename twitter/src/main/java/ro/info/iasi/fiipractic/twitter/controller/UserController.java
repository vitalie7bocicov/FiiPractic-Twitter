package ro.info.iasi.fiipractic.twitter.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.info.iasi.fiipractic.twitter.dto.request.FollowRequestDto;
import ro.info.iasi.fiipractic.twitter.dto.request.UnfollowRequestDto;
import ro.info.iasi.fiipractic.twitter.dto.request.UserRequestDto;
import ro.info.iasi.fiipractic.twitter.dto.response.UserResponseDto;
import ro.info.iasi.fiipractic.twitter.exception.FollowRelationshipAlreadyExistsException;
import ro.info.iasi.fiipractic.twitter.model.User;
import ro.info.iasi.fiipractic.twitter.service.FollowService;
import ro.info.iasi.fiipractic.twitter.service.UserService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
public class UserController {

    private UserService userService;
    private FollowService followService;

    public UserController(UserService userService, FollowService followService) {
        this.userService = userService;
        this.followService = followService;
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

    @PostMapping("/follow")
    public ResponseEntity<String> followUser(@Valid @RequestBody FollowRequestDto followRequestDto){
        String username = followRequestDto.getUsername();
        String usernameToFollow = followRequestDto.getUsernameToFollow();
        try {
            followService.saveFollow(username, usernameToFollow);
            return ResponseEntity.ok("'" + username +"' is now following '" + usernameToFollow +"'.");
        } catch (FollowRelationshipAlreadyExistsException e) {
            return ResponseEntity.ok("The follow relationship between '" + username + "' and '" + usernameToFollow + "' already exists.");
        }
    }

    @PostMapping("/unfollow")
    public ResponseEntity<String> unfollowUser(@Valid @RequestBody UnfollowRequestDto unFollowRequestDto){
        String username = unFollowRequestDto.getUsername();
        String usernameToUnfollow = unFollowRequestDto.getUsernameToUnfollow();
        followService.unFollow(username, usernameToUnfollow);
        return ResponseEntity.ok("'" + username +"' successfully unfollowed '" + usernameToUnfollow +"'.");
    }

    @DeleteMapping("unregister")
    public ResponseEntity<String> unregisterUser(@RequestParam String username){
        User user = userService.getByUsername(username);
        userService.deleteUser(user);
        return ResponseEntity.ok("'" + username + "' successfully unregistered!");
    }
}
