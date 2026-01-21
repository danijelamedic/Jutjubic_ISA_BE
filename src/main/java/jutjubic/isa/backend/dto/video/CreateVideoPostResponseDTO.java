package jutjubic.isa.backend.dto.video;

import java.time.LocalDateTime;

public class CreateVideoPostResponseDTO {

    private Long id;
    private String title;
    private String authorUsername;
    private LocalDateTime createdAt;

    public CreateVideoPostResponseDTO() {}

    public CreateVideoPostResponseDTO(Long id, String title, String authorUsername, LocalDateTime createdAt) {
        this.id = id;
        this.title = title;
        this.authorUsername = authorUsername;
        this.createdAt = createdAt;
    }

    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getAuthorUsername() { return authorUsername; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    public void setId(Long id) { this.id = id; }
    public void setTitle(String title) { this.title = title; }
    public void setAuthorUsername(String authorUsername) { this.authorUsername = authorUsername; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
