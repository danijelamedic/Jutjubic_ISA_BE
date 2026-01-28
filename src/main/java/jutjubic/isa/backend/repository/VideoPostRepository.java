package jutjubic.isa.backend.repository;

import jutjubic.isa.backend.model.VideoPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface VideoPostRepository extends JpaRepository<VideoPost, Long> {
    Page<VideoPost> findByAuthorUsernameOrderByCreatedAtDesc(String username, Pageable pageable);

    @Modifying
    @Query("UPDATE VideoPost v SET v.viewCount = v.viewCount + 1 WHERE v.id = :videoId")
    int incrementViewCount(@Param("videoId") Long videoId);

}
