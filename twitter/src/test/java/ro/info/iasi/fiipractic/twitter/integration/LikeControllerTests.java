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
import ro.info.iasi.fiipractic.twitter.dto.request.PostCRUDRequestDto;
import ro.info.iasi.fiipractic.twitter.model.Like;
import ro.info.iasi.fiipractic.twitter.model.Post;
import ro.info.iasi.fiipractic.twitter.model.User;
import ro.info.iasi.fiipractic.twitter.service.LikeService;
import ro.info.iasi.fiipractic.twitter.service.PostService;
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
class LikeControllerTests {

    @Autowired
    private LikeService likeService;

    @Autowired
    private UserService userService;

    @Autowired
    private PostService postService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Environment env;

    private User user;

    private Post post;

    @BeforeEach
    void setUp() {
        user = new User("username",
                "firstname",
                "lastname",
                "email@gmail.com",
                "password123");
        userService.saveUser(user);
        post = new Post(user,
                "post",
                System.currentTimeMillis());
        postService.savePost(post);
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
    void testLikePost() throws Exception {
        PostCRUDRequestDto likeDto = new PostCRUDRequestDto(user.getUsername(), post.getId());
        mockMvc.perform(MockMvcRequestBuilders.post("/likes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(likeDto)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print());
    }

    @Test
    void testLikePostAlreadyExists() throws Exception {
        PostCRUDRequestDto likeDto = new PostCRUDRequestDto(user.getUsername(), post.getId());
        likeService.saveLike(new Like(user, post));
        mockMvc.perform(MockMvcRequestBuilders.post("/likes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(likeDto)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andDo(print());
    }

    @Test
    void testRemoveLike() throws Exception {
        Like like = new Like(user, post);
        likeService.saveLike(like);

        PostCRUDRequestDto likeDto = new PostCRUDRequestDto(user.getUsername(), post.getId());

        mockMvc.perform(MockMvcRequestBuilders.delete("/likes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(likeDto)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print());
    }
}
