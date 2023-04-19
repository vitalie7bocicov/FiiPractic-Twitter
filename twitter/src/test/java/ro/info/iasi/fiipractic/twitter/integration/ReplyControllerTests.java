package ro.info.iasi.fiipractic.twitter.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
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
import ro.info.iasi.fiipractic.twitter.dto.request.ReplyRequestDto;
import ro.info.iasi.fiipractic.twitter.model.Post;
import ro.info.iasi.fiipractic.twitter.model.Reply;
import ro.info.iasi.fiipractic.twitter.model.User;
import ro.info.iasi.fiipractic.twitter.service.PostService;
import ro.info.iasi.fiipractic.twitter.service.ReplyService;
import ro.info.iasi.fiipractic.twitter.service.UserService;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class ReplyControllerTests {

    @Autowired
    private UserService userService;

    @Autowired
    private PostService postService;

    @Autowired
    private ReplyService replyService;

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
    public void testSaveReply() throws Exception {
        ReplyRequestDto replyDto = new ReplyRequestDto(user.getUsername(),
                "reply message",
                post.getId(),
                true);

        mockMvc.perform(MockMvcRequestBuilders.post("/replies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(replyDto)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print());
    }

    @Test
    public void testSaveReplyPostNotFound() throws Exception {
        ReplyRequestDto replyDto = new ReplyRequestDto(user.getUsername(),
                "reply message",
                UUID.randomUUID(),
                true);

        mockMvc.perform(MockMvcRequestBuilders.post("/replies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(replyDto)))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andDo(print());
    }

    @Test
    public void testGetReplies() throws Exception {

        Reply reply = new Reply(user, "Test reply message", System.currentTimeMillis(), post, true);
        replyService.saveReply(reply);

        PostCRUDRequestDto replyDto = new PostCRUDRequestDto(user.getUsername(), post.getId());

        mockMvc.perform(MockMvcRequestBuilders.get("/replies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(replyDto)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].message", Matchers.is("Test reply message")))
                .andDo(print());
    }

    @Test
    public void testGetRepliesNoContent() throws Exception {

        PostCRUDRequestDto replyDto = new PostCRUDRequestDto(user.getUsername(), post.getId());

        mockMvc.perform(MockMvcRequestBuilders.get("/replies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(replyDto)))
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andDo(print());
    }


}
