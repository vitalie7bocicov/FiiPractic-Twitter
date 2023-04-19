package ro.info.iasi.fiipractic.twitter.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.info.iasi.fiipractic.twitter.dto.request.*;
import ro.info.iasi.fiipractic.twitter.dto.response.UserResponseDto;
import ro.info.iasi.fiipractic.twitter.exception.FollowRelationshipAlreadyExistsException;
import ro.info.iasi.fiipractic.twitter.model.User;
import ro.info.iasi.fiipractic.twitter.service.FollowService;
import ro.info.iasi.fiipractic.twitter.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final FollowService followService;

    public UserController(UserService userService, FollowService followService) {
        this.userService = userService;
        this.followService = followService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@Valid @RequestBody UserRequestDto userRequestDto){
        User user = new User(userRequestDto.getUsername(),
                userRequestDto.getFirstname(),
                userRequestDto.getLastname(),
                userRequestDto.getPassword(),
                userRequestDto.getEmail());
        userService.saveUser(user);
        return ResponseEntity.ok("User " + user.getUsername() + " has been registered successfully!");
    }

    @PutMapping("/update")
    public ResponseEntity<String> updateUser(@Valid @RequestBody UserRequestDto userRequestDto){
        User user = userService.getByUsername(userRequestDto.getUsername());
        user.setFirstname(userRequestDto.getFirstname());
        user.setLastname(userRequestDto.getLastname());
        user.setEmail(userRequestDto.getEmail());
        user.setPassword(userRequestDto.getPassword());
        userService.updateUser(user);
        return ResponseEntity.ok("User " + user.getUsername() + " has been successfully updated!");
    }

    @GetMapping("/search")
    public ResponseEntity<List<UserResponseDto>> searchUsers(
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String firstname,
            @RequestParam(required = false) String lastname) {
        List<User> foundUsers = userService.searchUsers(username, firstname, lastname);
        List<UserResponseDto> users = foundUsers.stream()
                .map(user -> new UserResponseDto(user.getUsername(), user.getFirstname(), user.getLastname()))
                .toList();
        return ResponseEntity.ok(users);
    }

    @PostMapping("/follow")
    public ResponseEntity<String> followUser(@Valid @RequestBody FollowRequestDto followRequestDto){
        User user = userService.getByUsername(followRequestDto.getUsername());
        User followed = userService.getByUsername(followRequestDto.getUsernameToFollow());
        try {
            followService.saveFollow(user, followed);
            return ResponseEntity.ok("'" + user.getUsername() +"' is now following '"
                    + followed.getUsername() +"'.");
        } catch (FollowRelationshipAlreadyExistsException e) {
            return ResponseEntity.ok("The follow relationship between '"
                    + user.getUsername() + "' and '" + followed.getUsername() + "' already exists.");
        }
    }

    @PostMapping("/unfollow")
    public ResponseEntity<String> unfollowUser(@Valid @RequestBody UnfollowRequestDto unFollowRequestDto){
        User user = userService.getByUsername(unFollowRequestDto.getUsername());
        User followed = userService.getByUsername(unFollowRequestDto.getUsernameToUnfollow());
        followService.unFollow(user, followed);
        return ResponseEntity.ok("'" + user.getUsername()
                +"' successfully unfollowed '" + followed.getUsername() +"'.");
    }

    @PatchMapping("/security")
    public ResponseEntity<String> changePassword(@Valid @RequestBody UserPasswordUpdateRequestDto userRequestDto){
        User user = userService.getByUsername(userRequestDto.getUsername());
        userService.updateUserPassword(user, userRequestDto.getOldPassword(), userRequestDto.getNewPassword());
        return ResponseEntity.ok("The password for '" + user.getUsername() + "' has been successfully updated!");
    }

    @DeleteMapping("unregister")
    public ResponseEntity<String> unregisterUser(@Valid @RequestBody UserPasswordRequestDto userRequestDto){
        User user = userService.getByUsername(userRequestDto.getUsername());
        userService.deleteUser(user, userRequestDto.getPassword());
        return ResponseEntity.ok("'" + user.getUsername() + "' successfully unregistered!");
    }
}
