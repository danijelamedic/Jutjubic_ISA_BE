package jutjubic.isa.backend.service;

import jutjubic.isa.backend.dto.VideoCardDTO;
import jutjubic.isa.backend.model.VideoPost;
import jutjubic.isa.backend.repository.CommentRepository;
import jutjubic.isa.backend.repository.VideoLikeRepository;
import jutjubic.isa.backend.repository.VideoPostRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class PublicVideoService {

    private final VideoPostRepository videoPostRepository;
    private final VideoLikeRepository videoLikeRepository;
    private final CommentRepository commentRepository;

    public PublicVideoService(VideoPostRepository videoPostRepository,
                              VideoLikeRepository videoLikeRepository,
                              CommentRepository commentRepository) {
        this.videoPostRepository = videoPostRepository;
        this.videoLikeRepository = videoLikeRepository;
        this.commentRepository = commentRepository;
    }

    public Page<VideoCardDTO> getPublicVideos(int page, int size) {

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(Sort.Direction.DESC, "createdAt")
        );

        return videoPostRepository.findAll(pageable)
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
