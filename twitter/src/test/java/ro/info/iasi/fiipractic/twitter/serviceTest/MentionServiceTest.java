package ro.info.iasi.fiipractic.twitter.serviceTest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import ro.info.iasi.fiipractic.twitter.exception.BadRequestException;
import ro.info.iasi.fiipractic.twitter.model.Mention;
import ro.info.iasi.fiipractic.twitter.repository.MentionJpaRepository;
import ro.info.iasi.fiipractic.twitter.service.MentionService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MentionServiceTest {

    @InjectMocks
    MentionService mentionService;

    @Mock
    MentionJpaRepository  mentionRepository;

    @Before

    public void setUp() {
        mentionService = new MentionService(mentionRepository);
    }

    @Test
    public void testSaveMention(){
        Mention mention = mock(Mention.class);
        when(mentionRepository.save(mention)).thenReturn(mention);
        Mention result = mentionService.saveMention(mention);
        assertEquals(mention, result);
    }

    @Test
    public void testSaveMentionAlreadyExists(){
        Mention mention = mock(Mention.class);
        when(mentionRepository.findByUserAndPost(mention.getUser(), mention.getPost())).thenReturn(mention);
        assertThrows(BadRequestException.class, () -> mentionService.saveMention(mention));
    }

    @Test
    public void testGetMentionsByUser(){
        List<Mention> mentions = List.of(mock(Mention.class));
        when(mentionRepository.findMentionsByUser(mentions.get(0).getUser())).thenReturn(mentions);
        List<Mention> result = mentionService.getMentionsByUser(mentions.get(0).getUser());
        assertEquals(mentions, result);
    }

}
