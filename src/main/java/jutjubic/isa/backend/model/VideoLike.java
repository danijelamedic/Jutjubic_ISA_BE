package jutjubic.isa.backend.model;

import jakarta.persistence.*;

@Entity
@Table(
        name = "video_likes",
        uniqueConstraints = @UniqueConstraint(name = "uq_video_like_user_video", columnNames = {"user_id", "video_post_id"})
)
public class VideoLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "video_post_id", nullable = false)
    private VideoPost videoPost;

    public VideoLike() { }

    public VideoLike(User user, VideoPost videoPost) {
        this.user = user;
        this.videoPost = videoPost;
    }

    public Long getId() { return id; }
    public User getUser() { return user; }
    public VideoPost getVideoPost() { return videoPost; }

    public void setId(Long id) { this.id = id; }
    public void setUser(User user) { this.user = user; }
    public void setVideoPost(VideoPost videoPost) { this.videoPost = videoPost; }
}
