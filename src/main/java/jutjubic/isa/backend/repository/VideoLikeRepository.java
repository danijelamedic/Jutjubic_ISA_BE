package jutjubic.isa.backend.repository;

import jutjubic.isa.backend.model.VideoLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VideoLikeRepository extends JpaRepository<VideoLike, Long> {

    long countByVideoPostId(Long videoPostId);

    boolean existsByUserIdAndVideoPostId(Long userId, Long videoPostId);

    void deleteByUserIdAndVideoPostId(Long userId, Long videoPostId);
}
