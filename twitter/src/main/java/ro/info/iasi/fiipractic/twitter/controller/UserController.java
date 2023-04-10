package ro.info.iasi.fiipractic.twitter.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.info.iasi.fiipractic.twitter.dto.UserDto;
import ro.info.iasi.fiipractic.twitter.model.User;
import ro.info.iasi.fiipractic.twitter.service.UserService;

@RestController
@RequestMapping("/users")
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<String> getUsers(){
        return ResponseEntity.ok("HELLO WORLD!:)");
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@Valid @RequestBody UserDto userDto){
        User user = new User(userDto.getUsername(),
                userDto.getFirstname(),
                userDto.getLastname(),
                userDto.getEmail(),
                userDto.getEmail());
        userService.saveUser(user);

        return ResponseEntity.ok("User " + user.getId() + " has been registered successfully!");
    }


}
