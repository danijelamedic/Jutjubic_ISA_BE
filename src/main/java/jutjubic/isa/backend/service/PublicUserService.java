package jutjubic.isa.backend.service;

import jutjubic.isa.backend.dto.PublicUserDTO;
import jutjubic.isa.backend.dto.VideoCardDTO;
import jutjubic.isa.backend.model.User;
import jutjubic.isa.backend.model.VideoPost;
import jutjubic.isa.backend.repository.CommentRepository;
import jutjubic.isa.backend.repository.UserRepository;
import jutjubic.isa.backend.repository.VideoLikeRepository;
import jutjubic.isa.backend.repository.VideoPostRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PublicUserService {

    private final UserRepository userRepository;
    private final VideoPostRepository videoPostRepository;
    private final VideoLikeRepository videoLikeRepository;
    private final CommentRepository commentRepository;


    public PublicUserService(
            UserRepository userRepository,
            VideoPostRepository videoPostRepository,
            VideoLikeRepository videoLikeRepository,
            CommentRepository commentRepository
    ) {
        this.userRepository = userRepository;
        this.videoPostRepository = videoPostRepository;
        this.videoLikeRepository = videoLikeRepository;
        this.commentRepository = commentRepository;
    }


    @Transactional(readOnly = true)
    public PublicUserDTO getPublicUser(String username) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        return new PublicUserDTO(
                user.getUsername(),
                user.getFirstName(),
                user.getLastName()
        );
    }

    @Transactional(readOnly = true)
    public Page<VideoCardDTO> getPublicVideosByUser(String username, int page, int size) {

        if (userRepository.findByUsername(username).isEmpty()) {
            throw new IllegalArgumentException("User not found");
        }

        PageRequest pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));

        return videoPostRepository
                .findByAuthorUsernameOrderByCreatedAtDesc(username, pageable)
                .map(this::mapToDto);
    }

    private VideoCardDTO mapToDto(VideoPost videoPost) {
        long likeCount = videoLikeRepository.countByVideoPostId(videoPost.getId());
        long commentCount = commentRepository.countByVideoPostId(videoPost.getId());

        return new VideoCardDTO(
                videoPost.getId(),
                videoPost.getTitle(),
                videoPost.getAuthor().getUsername(),
                videoPost.getCreatedAt(),
                likeCount,
                commentCount
        );
    }

}
