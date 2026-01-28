package jutjubic.isa.backend.dto.video;

import java.time.LocalDateTime;

public class VideoDetailsDTO {

    private Long id;
    private String title;
    private String authorUsername;
    private LocalDateTime createdAt;
    private long likeCount;
    private long commentCount;
    private String location;
    private String description;
    private long viewCount;

    public VideoDetailsDTO() {
    }

    public VideoDetailsDTO(Long id, String title, String authorUsername,
                           LocalDateTime createdAt, long likeCount, long commentCount, String location, String description, long viewCount) {
        this.id = id;
        this.title = title;
        this.authorUsername = authorUsername;
        this.createdAt = createdAt;
        this.likeCount = likeCount;
        this.commentCount = commentCount;
        this.location = location;
        this.description = description;
        this.viewCount = viewCount;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getAuthorUsername() { return authorUsername; }
    public void setAuthorUsername(String authorUsername) { this.authorUsername = authorUsername; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public long getLikeCount() { return likeCount; }
    public void setLikeCount(long likeCount) { this.likeCount = likeCount; }

    public long getCommentCount() { return commentCount; }
    public void setCommentCount(long commentCount) { this.commentCount = commentCount; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public long getViewCount() { return viewCount; }
    public void setViewCount(long viewCount) { this.viewCount = viewCount; }
}
