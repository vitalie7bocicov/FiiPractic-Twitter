package ro.info.iasi.fiipractic.twitter.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ro.info.iasi.fiipractic.twitter.dto.request.*;
import ro.info.iasi.fiipractic.twitter.model.User;
import ro.info.iasi.fiipractic.twitter.service.FollowService;
import ro.info.iasi.fiipractic.twitter.service.UserService;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class UserControllerTests {
    @Autowired
    private UserService userService;

    @Autowired
    private FollowService followService;


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Environment env;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User("username",
                "firstname",
                "lastname",
                "email@gmail.com",
                "password123");
    }

    @AfterEach
    void tearDown() throws SQLException {
        String url = env.getProperty("spring.datasource.url");
        String username = env.getProperty("spring.datasource.username");
        String password = env.getProperty("spring.datasource.password");
        assert url != null;
        Connection connection = DriverManager.getConnection(url, username, password);
        Statement statement = connection.createStatement();
        statement.executeUpdate("DELETE FROM users");
        statement.close();
    }

    @Test
    void testRegisterUser() throws Exception {
        UserRequestDto userRequestDto = new UserRequestDto(user.getUsername(),
                user.getFirstname(), user.getLastname(), user.getEmail(), user.getPassword());
        mockMvc.perform(MockMvcRequestBuilders.post("/users/register")
                        .content(new ObjectMapper().writeValueAsString(userRequestDto))
                        .contentType("application/json")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print());
    }

    @Test
    void testRegisterUserBadPassword() throws Exception {
        UserRequestDto userRequestDto = new UserRequestDto(user.getUsername(),
                user.getFirstname(), user.getLastname(), user.getEmail(), "short");
        mockMvc.perform(MockMvcRequestBuilders.post("/users/register")
                        .content(new ObjectMapper().writeValueAsString(userRequestDto))
                        .contentType("application/json")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andDo(print());
    }

    @Test
    void testRegisterUserBadEmail() throws Exception {
        UserRequestDto userRequestDto = new UserRequestDto(user.getUsername(),
                user.getFirstname(), user.getLastname(), "badEmail", user.getPassword());
        mockMvc.perform(MockMvcRequestBuilders.post("/users/register")
                        .content(new ObjectMapper().writeValueAsString(userRequestDto))
                        .contentType("application/json")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andDo(print());
    }

    @Test
    void testUpdateUser() throws Exception {
        userService.saveUser(user);
        UserRequestDto userRequestDto = new UserRequestDto(user.getUsername(),
                "updated_firstname",
                "updated_lastname",
                "updated_email@gmail.com",
                "updated_password123");
        mockMvc.perform(MockMvcRequestBuilders.put("/users/update")
                        .content(new ObjectMapper().writeValueAsString(userRequestDto))
                        .contentType("application/json")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print());

        // Verify that the user has been updated
        User updatedUser = userService.getByUsername(user.getUsername());
        assertEquals(userRequestDto.getFirstname(), updatedUser.getFirstname());
        assertEquals(userRequestDto.getLastname(), updatedUser.getLastname());
        assertEquals(userRequestDto.getEmail(), updatedUser.getEmail());
    }

    @Test
    void testSearch() throws Exception {
        userService.saveUser(user);

        mockMvc.perform(MockMvcRequestBuilders.get("/users/search")
                        .param("username", user.getUsername())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].username").value(user.getUsername()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].firstname").value(user.getFirstname()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].lastname").value(user.getLastname()))
                .andDo(print());
    }

    @Test
    void testSearchNotFound() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/users/search")
                        .param("username", user.getUsername())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andDo(print());
    }

    @Test
    void testFollowUser() throws Exception {
        User userToFollow = new User("userToFollow",
                "firstname",
                "lastname",
                "email@email.com",
                "password123");
        userService.saveUser(user);
        userService.saveUser(userToFollow);
        FollowRequestDto followRequestDto = new FollowRequestDto(user.getUsername(), userToFollow.getUsername());

        mockMvc.perform(MockMvcRequestBuilders.post("/users/follow")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(followRequestDto)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print());
    }

    @Test
    void testFollowUser_alreadyFollowing() throws Exception {
        User userToFollow = new User("userToFollow",
                "firstname",
                "lastname",
                "email@email.com",
                "password123");
        userService.saveUser(user);
        userService.saveUser(userToFollow);
        FollowRequestDto followRequestDto = new FollowRequestDto(user.getUsername(), userToFollow.getUsername());
        followService.saveFollow(user, userToFollow);

        mockMvc.perform(MockMvcRequestBuilders.post("/users/follow")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(followRequestDto)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andDo(print());
    }

    @Test
    void testUnfollowUser() throws Exception {
        User userToUnfollow = new User("userToUnfollow",
                "firstname",
                "lastname",
                "email@email.com",
                "password123");
        userService.saveUser(user);
        userService.saveUser(userToUnfollow);
        followService.saveFollow(user, userToUnfollow);

        UnfollowRequestDto unfollowRequestDto = new UnfollowRequestDto(user.getUsername(), userToUnfollow.getUsername());

        mockMvc.perform(MockMvcRequestBuilders.post("/users/unfollow")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(unfollowRequestDto)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print());
    }

    @Test
    void testUnfollowUserNotFollowing() throws Exception {
        User userToUnfollow = new User("userToUnfollow",
                "firstname",
                "lastname",
                "email@email.com",
                "password123");
        userService.saveUser(user);
        userService.saveUser(userToUnfollow);

        UnfollowRequestDto unfollowRequestDto = new UnfollowRequestDto(user.getUsername(), userToUnfollow.getUsername());

        mockMvc.perform(MockMvcRequestBuilders.post("/users/unfollow")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(unfollowRequestDto)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andDo(print());
    }

    @Test
    void testChangePassword() throws Exception {
        userService.saveUser(user);
        UserPasswordUpdateRequestDto requestDto = new UserPasswordUpdateRequestDto(user.getUsername(),
                user.getPassword(),
                "newpassword456");

        mockMvc.perform(MockMvcRequestBuilders.patch("/users/security")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(requestDto)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print());
    }

    @Test
    void testChangePasswordBadPassword() throws Exception {
        userService.saveUser(user);
        UserPasswordUpdateRequestDto requestDto = new UserPasswordUpdateRequestDto(user.getUsername(),
                "badpassword",
                "newpassword456");

        mockMvc.perform(MockMvcRequestBuilders.patch("/users/security")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(requestDto)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andDo(print());
    }

    @Test
    void testUnregisterUser() throws Exception {
        userService.saveUser(user);
        UserPasswordRequestDto userRequestDto = new UserPasswordRequestDto(user.getUsername(), user.getPassword());

        mockMvc.perform(MockMvcRequestBuilders.delete("/users/unregister")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(userRequestDto)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print());
    }

    @Test
    void testUnregisterUserBadPassword() throws Exception {
        userService.saveUser(user);
        UserPasswordRequestDto userRequestDto = new UserPasswordRequestDto(user.getUsername(), "badPassword");

        mockMvc.perform(MockMvcRequestBuilders.delete("/users/unregister")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(userRequestDto)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andDo(print());
    }
}
