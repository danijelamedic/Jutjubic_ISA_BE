package jutjubic.isa.backend.messaging.dto;

import java.time.Instant;

public class UploadCreatedEventDTO {
    private Long videoId;
    private String title;
    private String authorUsername;
    private long videoSizeBytes;
    private long thumbnailSizeBytes;
    private Instant createdAt;

    public UploadCreatedEventDTO() {}

    public Long getVideoId() { return videoId; }
    public void setVideoId(Long videoId) { this.videoId = videoId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getAuthorUsername() { return authorUsername; }
    public void setAuthorUsername(String authorUsername) { this.authorUsername = authorUsername; }

    public long getVideoSizeBytes() { return videoSizeBytes; }
    public void setVideoSizeBytes(long videoSizeBytes) { this.videoSizeBytes = videoSizeBytes; }

    public long getThumbnailSizeBytes() { return thumbnailSizeBytes; }
    public void setThumbnailSizeBytes(long thumbnailSizeBytes) { this.thumbnailSizeBytes = thumbnailSizeBytes; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}
