package jutjubic.isa.backend.dto;

import java.time.LocalDateTime;

public class VideoCardDTO {

    private Long id;
    private String title;
    private String authorUsername;
    private LocalDateTime createdAt;
    private long likeCount;
    private long commentCount;

    public VideoCardDTO() {
    }

    public VideoCardDTO(Long id, String title, String authorUsername,
                        LocalDateTime createdAt, long likeCount, long commentCount) {
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

    public void setId(Long id) { this.id = id; }
    public void setTitle(String title) { this.title = title; }
    public void setAuthorUsername(String authorUsername) { this.authorUsername = authorUsername; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public void setLikeCount(long likeCount) { this.likeCount = likeCount; }
    public void setCommentCount(long commentCount) { this.commentCount = commentCount; }
}
