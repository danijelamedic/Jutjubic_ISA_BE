package jutjubic.isa.backend.service;

import jutjubic.isa.backend.dto.comment.CommentDTO;
import jutjubic.isa.backend.dto.video.VideoCardDTO;
import jutjubic.isa.backend.dto.video.VideoDetailsDTO;
import jutjubic.isa.backend.model.VideoPost;
import jutjubic.isa.backend.repository.CommentRepository;
import jutjubic.isa.backend.repository.VideoLikeRepository;
import jutjubic.isa.backend.repository.VideoPostRepository;
import jutjubic.isa.backend.service.storage.ThumbnailCacheService;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class PublicVideoService {

    private final VideoPostRepository videoPostRepository;
    private final VideoLikeRepository videoLikeRepository;
    private final CommentRepository commentRepository;
    private final ThumbnailCacheService thumbnailCacheService;
    private final CommentService commentService;

    public PublicVideoService(VideoPostRepository videoPostRepository,
                              VideoLikeRepository videoLikeRepository,
                              CommentRepository commentRepository,
                              ThumbnailCacheService thumbnailCacheService,
                              CommentService commentService) {
        this.videoPostRepository = videoPostRepository;
        this.videoLikeRepository = videoLikeRepository;
        this.commentRepository = commentRepository;
        this.thumbnailCacheService = thumbnailCacheService;
        this.commentService = commentService;
    }
    @Transactional(readOnly = true)
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

    @Transactional(readOnly = true)
    public Page<CommentDTO> getCommentsForVideo(Long videoId, int page, int size) {
        return commentService.getCommentsForVideo(videoId, page, size);
    }

    @Transactional(readOnly = true)
    public VideoDetailsDTO getVideoDetails(Long videoId) {

        VideoPost video = videoPostRepository.findById(videoId)
                .orElseThrow(() -> new IllegalArgumentException("Video not found"));

        long likeCount = videoLikeRepository.countByVideoPostId(videoId);
        long commentCount = commentRepository.countByVideoPostId(videoId);

        return new VideoDetailsDTO(
                video.getId(),
                video.getTitle(),
                video.getAuthor().getUsername(),
                video.getCreatedAt(),
                likeCount,
                commentCount,
                video.getLocation(),
                video.getDescription()
        );
    }

    @Transactional(readOnly = true)
    public ResponseEntity<byte[]> getThumbnailResponse(Long videoId) {
        VideoPost post = videoPostRepository.findById(videoId)
                .orElseThrow(() -> new IllegalArgumentException("Video not found"));

        byte[] bytes = thumbnailCacheService.getThumbnailBytes(post.getThumbnailPath());

        MediaType type = MediaType.IMAGE_JPEG;
        String path = post.getThumbnailPath().toLowerCase();
        if (path.endsWith(".png")) type = MediaType.IMAGE_PNG;
        if (path.endsWith(".webp")) type = MediaType.valueOf("image/webp");

        return ResponseEntity.ok().contentType(type).body(bytes);
    }

    @Transactional(readOnly = true)
    public ResponseEntity<Resource> getVideoStreamResponse(Long videoId) {

        VideoPost post = videoPostRepository.findById(videoId)
                .orElseThrow(() -> new IllegalArgumentException("Video not found"));

        String videoPath = post.getVideoPath();
        Path path = Paths.get(videoPath);

        Resource resource = new FileSystemResource(path);

        MediaType type = MediaType.valueOf("video/mp4");

        return ResponseEntity.ok()
                .contentType(type)
                .header(HttpHeaders.CACHE_CONTROL, "no-cache")
                .body(resource);
    }
}
