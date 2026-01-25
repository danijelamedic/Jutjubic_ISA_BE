package jutjubic.isa.backend.service;

import jutjubic.isa.backend.dto.comment.CommentDTO;
import jutjubic.isa.backend.exception.TooManyRequestsException;
import jutjubic.isa.backend.model.Comment;
import jutjubic.isa.backend.model.User;
import jutjubic.isa.backend.model.VideoPost;
import jutjubic.isa.backend.repository.CommentRepository;
import jutjubic.isa.backend.repository.VideoPostRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class CommentService {

    private static final int max_comments_per_hour = 60;
    private final CommentRepository commentRepository;
    private final VideoPostRepository videoPostRepository;

    public CommentService(CommentRepository commentRepository, VideoPostRepository videoPostRepository) {
        this.commentRepository = commentRepository;
        this.videoPostRepository = videoPostRepository;
    }

    @Cacheable(
            cacheNames = "videoComments",
            key = "'v:' + #videoId + ':p:' + #page + ':s:' + #size"
    )
    @Transactional(readOnly = true)
    public Page<CommentDTO> getCommentsForVideo(Long videoId, int page, int size) {
        if (!videoPostRepository.existsById(videoId)) {
            throw new IllegalArgumentException("Video not found");
        }
        // kesiranje provera
        System.out.println("HIT DB getCommentsForVideo " + videoId + " p=" + page + " s=" + size);

        PageRequest pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));

        return commentRepository.findByVideoPostId(videoId, pageable)
                .map(this::toDto);
    }

    @CacheEvict(cacheNames = "videoComments", allEntries = true)
    @Transactional
    public CommentDTO createComment(Long videoId, String text, User author) {
        if (author == null) {
            throw new IllegalArgumentException("User not found");
        }

        String normalized = (text == null) ? "" : text.trim();
        if (normalized.isEmpty()) {
            throw new IllegalArgumentException("Comment text is required");
        }
        if (normalized.length() > 1500) {
            throw new IllegalArgumentException("Comment text too long");
        }

        VideoPost videoPost = videoPostRepository.findById(videoId)
                .orElseThrow(() -> new IllegalArgumentException("Video not found"));

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime from = now.minusHours(1);

        long cntLastHour = commentRepository.countByAuthorIdAndCreatedAtBetween(author.getId(), from, now);
        if (cntLastHour >= max_comments_per_hour) {
            throw new TooManyRequestsException("Comment limit reached (60 per hour)");
        }

        Comment saved = commentRepository.save(new Comment(normalized, author, videoPost));
        return toDto(saved);
    }

    private CommentDTO toDto(Comment c) {
        return new CommentDTO(
                c.getId(),
                c.getText(),
                c.getCreatedAt(),
                c.getAuthor().getUsername()
        );
    }
}
