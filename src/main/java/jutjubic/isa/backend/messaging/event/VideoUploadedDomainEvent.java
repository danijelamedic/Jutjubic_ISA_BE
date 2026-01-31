package jutjubic.isa.backend.messaging.event;

import java.time.LocalDateTime;

public class VideoUploadedDomainEvent {

    private final Long videoId;
    private final String title;
    private final String authorUsername;
    private final long videoSizeBytes;
    private final long thumbnailSizeBytes;
    private final LocalDateTime createdAt;

    public VideoUploadedDomainEvent(
            Long videoId,
            String title,
            String authorUsername,
            long videoSizeBytes,
            long thumbnailSizeBytes,
            LocalDateTime createdAt
    ) {
        this.videoId = videoId;
        this.title = title;
        this.authorUsername = authorUsername;
        this.videoSizeBytes = videoSizeBytes;
        this.thumbnailSizeBytes = thumbnailSizeBytes;
        this.createdAt = createdAt;
    }

    public Long getVideoId() { return videoId; }
    public String getTitle() { return title; }
    public String getAuthorUsername() { return authorUsername; }
    public long getVideoSizeBytes() { return videoSizeBytes; }
    public long getThumbnailSizeBytes() { return thumbnailSizeBytes; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
