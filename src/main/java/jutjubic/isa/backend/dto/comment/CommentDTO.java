package jutjubic.isa.backend.dto.comment;

import java.time.LocalDateTime;

public class CommentDTO {

    private Long id;
    private String text;
    private LocalDateTime createdAt;
    private String authorUsername;

    public CommentDTO() {
    }

    public CommentDTO(Long id, String text, LocalDateTime createdAt, String authorUsername) {
        this.id = id;
        this.text = text;
        this.createdAt = createdAt;
        this.authorUsername = authorUsername;
    }

    public Long getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public String getAuthorUsername() {
        return authorUsername;
    }
}
