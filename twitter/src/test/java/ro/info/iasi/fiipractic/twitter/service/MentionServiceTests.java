package ro.info.iasi.fiipractic.twitter.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import ro.info.iasi.fiipractic.twitter.exception.BadRequestException;
import ro.info.iasi.fiipractic.twitter.model.Mention;
import ro.info.iasi.fiipractic.twitter.repository.MentionJpaRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class MentionServiceTests {

    @InjectMocks
    MentionService mentionService;

    @Mock
    MentionJpaRepository  mentionRepository;

    @BeforeEach

    public void setUp() {
        mentionService = new MentionService(mentionRepository);
    }

    @Test
    void testSaveMention(){
        Mention mention = mock(Mention.class);
        when(mentionRepository.save(mention)).thenReturn(mention);
        Mention result = mentionService.saveMention(mention);
        assertEquals(mention, result);
    }

    @Test
    void testSaveMentionAlreadyExists(){
        Mention mention = mock(Mention.class);
        when(mentionRepository.findByUserAndPost(mention.getUser(), mention.getPost())).thenReturn(mention);
        assertThrows(BadRequestException.class, () -> mentionService.saveMention(mention));
    }

    @Test
    void testGetMentionsByUser(){
        List<Mention> mentions = List.of(mock(Mention.class));
        when(mentionRepository.findMentionsByUser(mentions.get(0).getUser())).thenReturn(mentions);
        List<Mention> result = mentionService.getMentionsByUser(mentions.get(0).getUser());
        assertEquals(mentions, result);
    }

}
