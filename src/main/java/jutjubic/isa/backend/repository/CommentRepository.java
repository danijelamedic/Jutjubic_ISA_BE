package jutjubic.isa.backend.repository;

import jutjubic.isa.backend.model.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    Page<Comment> findByVideoPostId(Long videoPostId, Pageable pageable);

    long countByVideoPostId(Long videoPostId);
}
