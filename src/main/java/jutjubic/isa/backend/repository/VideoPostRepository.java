package jutjubic.isa.backend.repository;

import jutjubic.isa.backend.model.VideoPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface VideoPostRepository extends JpaRepository<VideoPost, Long> {
    Page<VideoPost> findByAuthorUsernameOrderByCreatedAtDesc(String username, Pageable pageable);
}
