package jutjubic.isa.backend.service;

import jakarta.transaction.Transactional;
import jutjubic.isa.backend.dto.video.CreateVideoPostRequestDTO;
import jutjubic.isa.backend.dto.video.CreateVideoPostResponseDTO;
import jutjubic.isa.backend.model.User;
import jutjubic.isa.backend.model.VideoPost;
import jutjubic.isa.backend.repository.UserRepository;
import jutjubic.isa.backend.repository.VideoPostRepository;
import jutjubic.isa.backend.service.storage.FileStorageService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class VideoPostService {

    private final UserRepository userRepository;
    private final VideoPostRepository videoPostRepository;
    private final FileStorageService fileStorageService;

    public VideoPostService(
            UserRepository userRepository,
            VideoPostRepository videoPostRepository,
            FileStorageService fileStorageService
    ) {
        this.userRepository = userRepository;
        this.videoPostRepository = videoPostRepository;
        this.fileStorageService = fileStorageService;
    }

    @Transactional
    public CreateVideoPostResponseDTO createVideoPost(
            String email,
            CreateVideoPostRequestDTO dto,
            MultipartFile video,
            MultipartFile thumbnail
    ) {

        User author = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        String videoPath = null;
        String thumbnailPath = null;

        try {
            // validacije
            validateVideo(video);
            validateThumbnail(thumbnail);

            // snimi fajlove
            videoPath = fileStorageService.storeVideo(video);
            thumbnailPath = fileStorageService.storeThumbnail(thumbnail);


//            // FORCED FAIL TEST
//            if (dto.getTitle().toLowerCase().contains("fail")) {
//                throw new RuntimeException("Forced failure for rollback test");
//            }


            // kreira VideoPost
            VideoPost post = new VideoPost();
            post.setTitle(dto.getTitle());
            post.setDescription(dto.getDescription());
            post.setTags(dto.getTags());
            post.setLocation(dto.getLocation());
            post.setVideoPath(videoPath);
            post.setThumbnailPath(thumbnailPath);
            post.setAuthor(author);

            videoPostRepository.save(post);

            return new CreateVideoPostResponseDTO(
                    post.getId(),
                    post.getTitle(),
                    author.getUsername(),
                    post.getCreatedAt()
            );

        } catch (Exception e) {
            // rollback
            if (videoPath != null) {
                fileStorageService.deleteFile(videoPath);
            }
            if (thumbnailPath != null) {
                fileStorageService.deleteFile(thumbnailPath);
            }
            throw e;
        }
    }

    private void validateVideo(MultipartFile video) {
        if (video.isEmpty()) {
            throw new IllegalArgumentException("Video file is required");
        }
        if (!"video/mp4".equals(video.getContentType())) {
            throw new IllegalArgumentException("Only MP4 videos are allowed");
        }
        if (video.getSize() > 200L * 1024 * 1024) {
            throw new IllegalArgumentException("Video exceeds max size of 200MB");
        }
    }

    private void validateThumbnail(MultipartFile thumbnail) {
        if (thumbnail.isEmpty()) {
            throw new IllegalArgumentException("Thumbnail is required");
        }
        if (!thumbnail.getContentType().startsWith("image/")) {
            throw new IllegalArgumentException("Thumbnail must be an image");
        }
    }
}
