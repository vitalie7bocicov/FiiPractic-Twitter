package ro.info.iasi.fiipractic.twitter.integration;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
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
import ro.info.iasi.fiipractic.twitter.controller.UserController;
import ro.info.iasi.fiipractic.twitter.dto.request.UserRequestDto;
import ro.info.iasi.fiipractic.twitter.model.User;
import ro.info.iasi.fiipractic.twitter.service.FollowService;
import ro.info.iasi.fiipractic.twitter.service.UserService;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class UserControllerTests {

    @Mock
    private UserService userService;

    @Mock
    private FollowService followService;

    @InjectMocks
    private UserController userController;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Environment env;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User("username", "firstname", "lastname", "email@gmail.com", "password123");
        userController = new UserController(userService, followService);
    }

    @AfterEach
    void tearDown() throws SQLException {
        String url = env.getProperty("spring.datasource.url");
        String username = env.getProperty("spring.datasource.username");
        String password = env.getProperty("spring.datasource.password");
        assert url != null;
        Connection connection = DriverManager.getConnection(url, username, password);
        Statement statement = connection.createStatement();
        statement.executeUpdate("DROP TABLE IF EXISTS followers");
        statement.executeUpdate("DROP TABLE IF EXISTS likes");
        statement.executeUpdate("DROP TABLE IF EXISTS mentions");
        statement.executeUpdate("DROP TABLE IF EXISTS posts");
        statement.executeUpdate("DROP TABLE IF EXISTS users");

    }

    @Test
    void testRegisterUser() throws Exception {
        UserRequestDto userRequestDto = new UserRequestDto(user.getUsername(),
                user.getFirstname(), user.getLastname(), user.getEmail(), user.getPassword());
        mockMvc.perform(MockMvcRequestBuilders.post("/users/register")
                        .content("{\"username\":\"" + userRequestDto.getUsername() +
                                "\",\"firstname\":\"" + userRequestDto.getFirstname() +
                                "\",\"lastname\":\"" + userRequestDto.getLastname() +
                                "\",\"email\":\"" + userRequestDto.getEmail() +
                                "\",\"password\":\"" + userRequestDto.getPassword() + "\"}")
                        .contentType("application/json")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print());
    }
}
