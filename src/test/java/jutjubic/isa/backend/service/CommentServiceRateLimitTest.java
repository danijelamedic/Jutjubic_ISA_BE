package jutjubic.isa.backend.service;

import jutjubic.isa.backend.exception.TooManyRequestsException;
import jutjubic.isa.backend.model.User;
import jutjubic.isa.backend.model.VideoPost;
import jutjubic.isa.backend.repository.CommentRepository;
import jutjubic.isa.backend.repository.VideoPostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class CommentServiceRateLimitTest {

    private CommentRepository commentRepository;
    private VideoPostRepository videoPostRepository;
    private CommentService commentService;

    @BeforeEach
    void setUp() {
        commentRepository = mock(CommentRepository.class);
        videoPostRepository = mock(VideoPostRepository.class);
        commentService = new CommentService(commentRepository, videoPostRepository);
    }

    @Test
    void shouldThrowWhenLimitReached_60PerHour() {
        Long videoId = 10L;

        User author = new User();
        author.setId(4L);
        author.setUsername("daca");

        VideoPost vp = new VideoPost();
        vp.setId(videoId);

        when(videoPostRepository.findById(videoId)).thenReturn(Optional.of(vp));

        // simuliramo da je korisnik već poslao 60 komentara u poslednjih sat vremena
        when(commentRepository.countByAuthorIdAndCreatedAtBetween(
                eq(author.getId()),
                any(LocalDateTime.class),
                any(LocalDateTime.class)
        )).thenReturn(60L);

        assertThrows(
                TooManyRequestsException.class,
                () -> commentService.createComment(videoId, "test", author)
        );


        // verify da nije pokušao save
        verify(commentRepository, never()).save(any());
    }

    @Test
    void shouldAllowWhenUnderLimit() {
        Long videoId = 10L;

        User author = new User();
        author.setId(4L);
        author.setUsername("daca");

        VideoPost vp = new VideoPost();
        vp.setId(videoId);

        when(videoPostRepository.findById(videoId)).thenReturn(Optional.of(vp));

        when(commentRepository.countByAuthorIdAndCreatedAtBetween(
                eq(author.getId()),
                any(LocalDateTime.class),
                any(LocalDateTime.class)
        )).thenReturn(59L);

        // da ne pišemo ceo Comment konstruktor ovde, samo proveravamo da save bude pozvan
        when(commentRepository.save(ArgumentMatchers.any()))
                .thenAnswer(inv -> inv.getArgument(0));

        commentService.createComment(videoId, "ok", author);

        verify(commentRepository, times(1)).save(any());
    }
}
