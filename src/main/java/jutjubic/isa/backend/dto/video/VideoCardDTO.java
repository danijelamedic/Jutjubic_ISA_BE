package jutjubic.isa.backend.dto.video;

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
}
