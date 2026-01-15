package jutjubic.isa.backend.dto;

import java.time.LocalDateTime;

public class VideoDetailsDTO {

    private Long id;
    private String title;
    private String authorUsername;
    private LocalDateTime createdAt;
    private long likeCount;
    private long commentCount;

    public VideoDetailsDTO() {}

    public VideoDetailsDTO(Long id, String title, String authorUsername, LocalDateTime createdAt, long likeCount, long commentCount) {
        this.id = id;
        this.title = title;
        this.authorUsername = authorUsername;
        this.createdAt = createdAt;
        this.likeCount = likeCount;
        this.commentCount = commentCount;
    }

    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getAuthorUsername() { return authorUsername; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public long getLikeCount() { return likeCount; }
    public long getCommentCount() { return commentCount; }
}
