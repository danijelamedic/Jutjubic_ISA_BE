package jutjubic.isa.backend.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "video_posts")
public class VideoPost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String title;

    @Column(nullable = false, length = 2000)
    private String description;

    // tagovi csv string, npr. "food,travel,funny"
    @Column(nullable = false, length = 500)
    private String tags;

    // lokalno cuvanje fajlova: u bazi su samo putanje
    @Column(nullable = false, length = 500)
    private String videoPath;

    @Column(nullable = false, length = 500)
    private String thumbnailPath;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = true)
    private LocalDateTime scheduledAt;

    // geolocation
    @Column(nullable = true, length = 255)
    private String location;

    @Column(name = "view_count", nullable = false)
    private long viewCount;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    public VideoPost() { }

    @PrePersist
    public void prePersist() {
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
        if (this.tags == null) {
            this.tags = "";
        }
    }

    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getTags() { return tags; }
    public String getVideoPath() { return videoPath; }
    public String getThumbnailPath() { return thumbnailPath; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getScheduledAt() { return scheduledAt; }
    public String getLocation() { return location; }
    public User getAuthor() { return author; }
    public long getViewCount() { return viewCount; }


    public void setId(Long id) { this.id = id; }
    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }
    public void setTags(String tags) { this.tags = tags; }
    public void setVideoPath(String videoPath) { this.videoPath = videoPath; }
    public void setThumbnailPath(String thumbnailPath) { this.thumbnailPath = thumbnailPath; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public void setScheduledAt(LocalDateTime scheduledAt) { this.scheduledAt = scheduledAt; }
    public void setLocation(String location) { this.location = location; }
    public void setAuthor(User author) { this.author = author; }
    public void setViewCount(long viewCount) { this.viewCount = viewCount; }
}
