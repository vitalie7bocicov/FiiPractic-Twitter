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
import ro.info.iasi.fiipractic.twitter.dto.request.PostRequestDto;
import ro.info.iasi.fiipractic.twitter.model.Mention;
import ro.info.iasi.fiipractic.twitter.model.Post;
import ro.info.iasi.fiipractic.twitter.model.User;
import ro.info.iasi.fiipractic.twitter.service.FollowService;
import ro.info.iasi.fiipractic.twitter.service.MentionService;
import ro.info.iasi.fiipractic.twitter.service.PostService;
import ro.info.iasi.fiipractic.twitter.service.UserService;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Instant;
import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class PostControllerTests {

    @Autowired
    private UserService userService;

    @Autowired
    private PostService postService;

    @Autowired
    private MentionService mentionService;

    @Autowired
    private FollowService followService;

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
    void testSavePost() throws Exception {
        PostRequestDto postRequestDto = new PostRequestDto(user.getUsername(), post.getMessage());
        mockMvc.perform(MockMvcRequestBuilders.post("/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(postRequestDto)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print());
    }

    @Test
    void testRepost() throws Exception {
        Post savedPost = postService.savePost(post);
        PostCRUDRequestDto postDto = new PostCRUDRequestDto(user.getUsername(), savedPost.getId());
        mockMvc.perform(MockMvcRequestBuilders.post("/posts/repost")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(postDto)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print());
    }

    @Test
    void testRepostPostNotFound() throws Exception {
        PostCRUDRequestDto postDto = new PostCRUDRequestDto(user.getUsername(), UUID.randomUUID());
        mockMvc.perform(MockMvcRequestBuilders.post("/posts/repost")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(postDto)))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andDo(print());
    }

    @Test
    void testDeletePost() throws Exception {
        Post savedPost = postService.savePost(post);

        mockMvc.perform(MockMvcRequestBuilders.delete("/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(new PostCRUDRequestDto(user.getUsername(), savedPost.getId()))))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print());
    }

    @Test
    void testDeletePostUnauthorized() throws Exception {
        Post savedPost = postService.savePost(post);
        User notAuthor = new User("notAuthor",
                "firstname",
                "lastname",
                "email@email.com",
                "password123");
        userService.saveUser(notAuthor);
        savedPost.setUser(notAuthor);
        mockMvc.perform(MockMvcRequestBuilders.delete("/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(new PostCRUDRequestDto(notAuthor.getUsername(), savedPost.getId()))))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized())
                .andDo(print());
    }

    @Test
    void testGetOwnPosts() throws Exception {
        Instant now = Instant.now();
        Post post1 = new Post(user, "Test post 1", now.toEpochMilli());
        Post post2 = new Post(user, "Test post 2", now.minusSeconds(30).toEpochMilli());
        Post post3 = new Post(user, "Test post 3", now.minusSeconds(60).toEpochMilli());
        postService.savePost(post1);
        postService.savePost(post2);
        postService.savePost(post3);

        mockMvc.perform(MockMvcRequestBuilders.get("/posts")
                        .param("username", user.getUsername())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(3)).andDo(print());

        mockMvc.perform(MockMvcRequestBuilders.get("/posts")
                        .param("username", user.getUsername())
                        .param("timestamp", now.minusSeconds(45).toString())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(2))
                .andDo(print());

    }

    @Test
    void testGetOwnPostsWithTimeFilter() throws Exception {
        Instant now = Instant.now();
        Post post1 = new Post(user, "Test post 1", now.toEpochMilli());
        Post post2 = new Post(user, "Test post 2", now.minusSeconds(30).toEpochMilli());
        Post post3 = new Post(user, "Test post 3", now.minusSeconds(60).toEpochMilli());
        postService.savePost(post1);
        postService.savePost(post2);
        postService.savePost(post3);

        mockMvc.perform(MockMvcRequestBuilders.get("/posts")
                        .param("username", user.getUsername())
                        .param("timestamp", now.minusSeconds(45).toString())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(2))
                .andDo(print());

    }
    @Test
    void testGetOwnPostsWithNotContent() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/posts")
                        .param("username", "username")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

    }

    @Test
    void testGetFeed() throws Exception {
        User user1 = new User("user1", "first1", "last1", "email1@email.com", "password123");
        User user2 = new User("user2", "first2", "last2", "email2@email.com", "password2232");
        userService.saveUser(user1);
        userService.saveUser(user2);

        Post post1 = new Post(user2, "message1", System.currentTimeMillis());
        Post post2 = new Post(user2, "message2", System.currentTimeMillis());
        Post post3 = new Post(user2, "message3", System.currentTimeMillis());
        postService.savePost(post1);
        postService.savePost(post2);
        postService.savePost(post3);

        followService.saveFollow(user1, user2);

        mockMvc.perform(MockMvcRequestBuilders.get("/posts/feed")
                        .param("username", user1.getUsername()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(3)))
                .andDo(print());
    }

    @Test
    void testGetFeedNoContent() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/posts/feed")
                        .param("username", user.getUsername()))
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andDo(print());
    }

    @Test
    void testSaveMention() throws Exception {
        postService.savePost(post);

        PostCRUDRequestDto mentionDto = new PostCRUDRequestDto(user.getUsername(), post.getId());

        mockMvc.perform(MockMvcRequestBuilders.post("/posts/mentions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(mentionDto)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print());
    }

    @Test public void testSaveMentionAlreadyExists() throws Exception {
        postService.savePost(post);

        PostCRUDRequestDto mentionDto = new PostCRUDRequestDto(user.getUsername(), post.getId());
        mentionService.saveMention(new Mention(user, post));
        mockMvc.perform(MockMvcRequestBuilders.post("/posts/mentions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(mentionDto)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andDo(print());
    }

    @Test
    void testGetAllMentions() throws Exception {
        User user1 = new User("testUser1",
                "Test1",
                "User1",
                "testUser1@example.com",
                "password123");
        userService.saveUser(user1);
        Post post1 = new Post(user1, "Test post message 1", System.currentTimeMillis());
        postService.savePost(post1);

        User user2 = new User("testUser2",
                "Test2",
                "User2",
                "testUser2@example.com",
                "password123");
        userService.saveUser(user2);
        Post post2 = new Post(user2, "Test post message 2", System.currentTimeMillis());
        postService.savePost(post2);

        Mention mention = new Mention(user2, post1);
        mentionService.saveMention(mention);

        mention = new Mention(user1, post2);
        mentionService.saveMention(mention);

        mockMvc.perform(MockMvcRequestBuilders.get("/posts/mentions")
                        .param("username", "testUser1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].message").value(post2.getMessage()))
                .andDo(print());

        mockMvc.perform(MockMvcRequestBuilders.get("/posts/mentions")
                        .param("username", "testUser2"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].message").value(post1.getMessage()))
                .andDo(print());
    }
    @Test
    void testGetAllMentionsNoContent() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/posts/mentions")
                        .param("username", "username"))
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andDo(print());
    }
}
